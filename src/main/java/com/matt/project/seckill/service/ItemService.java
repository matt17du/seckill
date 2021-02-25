package com.matt.project.seckill.service;



import com.matt.project.seckill.error.BusinessException;
import com.matt.project.seckill.service.model.ItemModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author matt
 * @create 2020-12-08 13:57
 */
public interface ItemService {



    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //item及promo model缓存模型
    ItemModel getItemByIdInCache(Integer id);

    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount)throws BusinessException;
    //库存回补
    boolean increaseStock(Integer itemId,Integer amount)throws BusinessException;

    //异步更新库存
    boolean asyncDecreaseStock(Integer itemId,Integer amount) throws UnsupportedEncodingException;

    //商品销量增加
    void increaseSales(Integer itemId,Integer amount)throws BusinessException;

    //初始化库存流水
    String initStockLog(Integer itemId,Integer amount);


}
