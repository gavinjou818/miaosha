package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.mq.MqProducer;
import com.miaoshaproject.reponse.CommonReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.*;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials ="true",allowedHeaders = "*")
public class OrderController extends BaseController
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MqProducer mqProducer;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PromoService promoService;


    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
        executorService = Executors.newFixedThreadPool(20);
    }

    //生成秒杀令牌
    @RequestMapping(value = "/generatetoken",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType generatetoken(@RequestParam(name="itemId")Integer itemId,
                                          @RequestParam(name="promoId")Integer promoId) throws BusinessException {

        String token = httpServletRequest.getParameterMap().get("token")[0];
        if(StringUtils.isEmpty(token))
        {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
        }

        // 获取用户的登录信息
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if(userModel == null)
        {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
        }

        // 获取秒杀访问令牌
        String promoToken = promoService.generateSecondKillToken(promoId,itemId,userModel.getId());

        if(promoToken == null)
        {
            throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR,"生成令牌失败");
        }

        //返回对应的结果
        return CommonReturnType.create(promoToken);
    }

    // 封装下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId",required = false) Integer promoId,
                                        @RequestParam(name = "promoToken",required = false) String promoToken
                                            ) throws BusinessException {

        String token = httpServletRequest.getParameterMap().get("token")[0];
        if(StringUtils.isEmpty(token))
        {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登录");
        }

        // 获取用户的登录信息
//        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if(userModel == null)
        {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
        }

        //校验秒杀令牌是否正确
        if(promoId != null)
        {
            String inRedisPromoToken = (String) redisTemplate.opsForValue().get("promo_token_" + promoId + "_userid_" + userModel.getId() + "_itemid_" + itemId);
            if(inRedisPromoToken == null)
            {
                throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR,"秒杀令牌校验失败");
            }

            if(!org.apache.commons.lang3.StringUtils.equals(promoToken,inRedisPromoToken))
            {
                throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR,"秒杀令牌校验失败");
            }
        }

        // 同步调用线程池的submit方法
        // 拥塞窗口位20的等待队列，用来队列化泄洪
        Future<Object> future = executorService.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception
            {
                // 加入库存流水init状态
                String stockLogId = itemService.initStockLog(itemId,amount);

                // 再去完成对应的下单事务型消息机制
                if(!mqProducer.transactionAsyncReduceStock(userModel.getId(),promoId,itemId,amount,stockLogId))
                {
                    throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"下单失败");
                }

                return null;
            }
        });

        try
        {
            future.get();
        }
        catch (InterruptedException e)
        {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
        catch (ExecutionException e)
        {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }


        return CommonReturnType.create(null);
    }
}
