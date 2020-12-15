package com.matt.project.seckill.service.impl;

import com.matt.project.seckill.dao.ItemDOMapper;
import com.matt.project.seckill.dao.PromoDOMapper;
import com.matt.project.seckill.dataobject.ItemDO;
import com.matt.project.seckill.dataobject.PromoDO;
import com.matt.project.seckill.service.PromoService;
import com.matt.project.seckill.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.util.calendar.LocalGregorianCalendar;

import java.math.BigDecimal;

/**
 * @author matt
 * @create 2020-12-13 13:28
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Transactional
    @Override
    public PromoModel getPromoByItemId(Integer itemId) {

        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);




        PromoModel promoModel = convertModelFromDO(promoDO);
        return promoModel;
    }

    public PromoModel convertModelFromDO(PromoDO promoDO) {

        if(promoDO == null) {
            return null;
        }

        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);

        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));

        DateTime dateTime = DateTime.now();
        // 设置商品的状态
        if (promoModel.getEndDate().isBefore(dateTime)){
            promoModel.setStatus(2);
        } else if (promoModel.getStartDate().isBefore(dateTime)){
            promoModel.setStatus(0);
        } else {
            promoModel.setStatus(1);
        }

        return promoModel;

    }
}