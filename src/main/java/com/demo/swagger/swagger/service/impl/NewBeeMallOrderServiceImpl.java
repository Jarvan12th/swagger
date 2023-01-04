package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.controller.vo.NewBeeMallShoppingCartItemVO;
import com.demo.swagger.swagger.dao.*;
import com.demo.swagger.swagger.entity.*;
import com.demo.swagger.swagger.service.NewBeeMallOrderService;
import com.demo.swagger.swagger.utils.BeanUtils;
import com.demo.swagger.swagger.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NewBeeMallOrderServiceImpl implements NewBeeMallOrderService {

    @Autowired
    private NewBeeMallOrderMapper newBeeMallOrderMapper;

    @Autowired
    private NewBeeMallOrderItemMapper newBeeMallOrderItemMapper;

    @Autowired
    private NewBeeMallShoppingCartItemMapper newBeeMallShoppingCartItemMapper;

    @Autowired
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

    @Autowired
    private NewBeeMallOrderAddressMapper newBeeMallOrderAddressMapper;

    @Override
    @Transactional
    public String saveOrder(MallUser mallUser, MallUserAddress mallUserAddress, List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS) {
        List<Long> goodsIds = newBeeMallShoppingCartItemVOS.stream().map(NewBeeMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<NewBeeMallGoods> newBeeMallGoods = newBeeMallGoodsMapper.selectGoodsByPrimaryKeys(goodsIds);
        List<NewBeeMallGoods> goodsNotSelling = newBeeMallGoods.stream()
                .filter(g -> g.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsNotSelling)) {
            NewBeeMallException.fail(goodsNotSelling.get(0).getGoodsName() + "has been removed off shelves");
        }

        Map<Long, NewBeeMallGoods> newBeeMallGoodsMap = newBeeMallGoods.stream()
                .collect(Collectors.toMap(NewBeeMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        for (NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO : newBeeMallShoppingCartItemVOS) {
            if (!newBeeMallGoodsMap.containsKey(newBeeMallShoppingCartItemVO.getGoodsId())) {
                NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            if (newBeeMallShoppingCartItemVO.getGoodsCount() > newBeeMallGoodsMap.get(newBeeMallShoppingCartItemVO.getGoodsId()).getStockNum()) {
                NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }

        List<Long> cartItemIds = newBeeMallShoppingCartItemVOS.stream().map(NewBeeMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cartItemIds) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(newBeeMallGoods)) {
            if (newBeeMallShoppingCartItemMapper.batchDelete(cartItemIds) > 0) {
                List<GoodsStock> goodsStocks = BeanUtils.copyList(newBeeMallShoppingCartItemVOS, GoodsStock.class);
                int updateGoodsStockResult = newBeeMallGoodsMapper.updateGoodsStock(goodsStocks);
                if (updateGoodsStockResult < 1) {
                    NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }

                String orderNo = NumberUtils.generateOrderNo();
                NewBeeMallOrder newBeeMallOrder = new NewBeeMallOrder();
                newBeeMallOrder.setOrderNo(orderNo);
                newBeeMallOrder.setUserId(mallUser.getUserId());

                int priceTotal = 0;
                for (NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO : newBeeMallShoppingCartItemVOS) {
                    priceTotal += newBeeMallShoppingCartItemVO.getGoodsCount() * newBeeMallShoppingCartItemVO.getSellingPrice();
                }
                newBeeMallOrder.setTotalPrice(priceTotal);
                newBeeMallOrder.setExtraInfo("");

                if (newBeeMallOrderMapper.insertSelective(newBeeMallOrder) > 0) {
                    Long orderId = newBeeMallOrderMapper.selectLastInsertAutoIncrementId();
                    NewBeeMallOrderAddress newBeeMallOrderAddress = new NewBeeMallOrderAddress();
                    BeanUtils.copyProperties(mallUserAddress, newBeeMallOrderAddress);
                    newBeeMallOrderAddress.setOrderId(orderId);

                    List<NewBeeMallOrderItem> newBeeMallOrderItems = new ArrayList<>();
                    for (NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO : newBeeMallShoppingCartItemVOS) {
                        NewBeeMallOrderItem newBeeMallOrderItem = new NewBeeMallOrderItem();
                        BeanUtils.copyProperties(newBeeMallShoppingCartItemVO, newBeeMallOrderItem);
                        newBeeMallOrderItem.setOrderId(orderId);
                        newBeeMallOrderItems.add(newBeeMallOrderItem);
                    }

                    if (newBeeMallOrderItemMapper.batchInsert(newBeeMallOrderItems) > 0
                            && newBeeMallOrderAddressMapper.insertSelective(newBeeMallOrderAddress) > 0) {
                        return orderNo;
                    }
                    NewBeeMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }
}
