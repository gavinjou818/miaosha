package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

public interface PromoService {

    // 根据itemid获取即将进行的或者正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);

    // 活动发布
    void publishPromo(Integer promoId);

    //生成秒杀用的令牌，同时验证用户信息和商品信息
    String generateSecondKillToken(Integer promoId,Integer itemId,Integer userId);
}
