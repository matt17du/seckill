package com.matt.project.seckill.service;

import com.matt.project.seckill.service.model.PromoModel;

/**
 * @author matt
 * @create 2020-12-13 13:27
 */
public interface PromoService {

    /**
     * 功能：根据商品ID查询对应的活动
     * @author matt
     * @date 2020/12/15
     * @param itemId
     * @return com.matt.project.seckill.service.model.PromoModel
    */
    PromoModel getPromoByItemId(Integer itemId);
}