package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.OrderDOMapper;
import com.miaoshaproject.dao.SequenceDOMapper;
import com.miaoshaproject.dataobject.OrderDO;
import com.miaoshaproject.dataobject.SequenceDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService
{

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;


    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {

        //1.校验下单状态，下单的商品是否存在，用户是否合法，购买数量是否正确
//        ItemModel itemModel = itemService.getItemById(itemId);
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);

        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

//        UserModel userModel = userService.getUserById(userId);
        UserModel userModel = userService.getUserByIdInCache(userId);
        if (userModel == null)
        {
            throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR, "用户信息不存在");
        }

        //2.落单减库存
        boolean result = itemService.decreaseStock(itemId,amount);
        if(!result)
        {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        if(promoId != null)
        {
            // 校验对应活动是否存在这个适用商品
            if(promoId.intValue() != itemModel.getPromoModel().getId())
            {
                throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR, "活动信息不正确");
            }
            else if (itemModel.getPromoModel().getStatus().intValue() != 2) {
                throw new BusinessException(EmBusinessError.PAPAMETER_VALIDATION_ERROR, "活动还未开始");
            }
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());//取秒杀价格
        }else {
            orderModel.setItemPrice(itemModel.getPrice());//取平销价格
        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        // 生成交易流水号
        orderModel.setId(generateOrderNo());
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        // 加上商品的销量
        itemService.increaseSales(itemId,amount);
        //4.返回前端
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo()
    {
        //订单有16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        // 中间为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        //最后两位为分库分表位,暂时不考虑
        stringBuilder.append("00");

        return  stringBuilder.toString();
    }


    private OrderDO convertFromOrderModel(OrderModel orderModel)
    {
        if(orderModel == null)
        {
            return null;
        }

        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
