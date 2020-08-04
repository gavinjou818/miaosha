package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.PromoDOMapper;
import com.miaoshaproject.dataobject.PromoDO;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService
{

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PromoModel getPromoByItemId(Integer itemId)
    {
        // 获取对应商品的秒杀活动信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel == null)
        {
            return null;
        }

        // 判断当前时间是否在秒杀活动即将开始或正在进行
        if(promoModel.getStartDate().isAfterNow())
        {
            promoModel.setStatus(1);
        }
        else if(promoModel.getEndDate().isBeforeNow())
        {
            promoModel.setStatus(3);
        }
        else
        {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    @Override
    public void publishPromo(Integer promoId)
    {
        //通过活动id获取活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if(promoDO.getItemId() == null || promoDO.getItemId().intValue() == 0)
        {
            return;
        }

        ItemModel itemModel = itemService.getItemById(promoDO.getItemId());

        //将库存同步到redis内
        redisTemplate.opsForValue().set("promo_item_stock_"+itemModel.getId(), itemModel.getStock());
    }

    private PromoModel convertFromDataObject(PromoDO promoDO)
    {
        if(promoDO == null)
        {
            return null;
        }

        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));

        return promoModel;
    }
}
