package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.common.*;
import com.demo.swagger.swagger.controller.vo.NewBeeMallOrderDetailVO;
import com.demo.swagger.swagger.controller.vo.NewBeeMallOrderItemVO;
import com.demo.swagger.swagger.controller.vo.NewBeeMallOrderListVO;
import com.demo.swagger.swagger.controller.vo.NewBeeMallShoppingCartItemVO;
import com.demo.swagger.swagger.dao.*;
import com.demo.swagger.swagger.entity.*;
import com.demo.swagger.swagger.service.NewBeeMallOrderService;
import com.demo.swagger.swagger.utils.BeanUtils;
import com.demo.swagger.swagger.utils.NumberUtils;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Override
    public String paySuccess(String orderNo, int payType) {
        NewBeeMallOrder newBeeMallOrder = newBeeMallOrderMapper.selectByOrderNo(orderNo);
        if (newBeeMallOrder != null) {
            if (newBeeMallOrder.getOrderStatus().intValue() != NewBeeMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                NewBeeMallException.fail("pay forbidden");
            }

            newBeeMallOrder.setOrderStatus((byte) NewBeeMallOrderStatusEnum.ORDER_PAID.getOrderStatus());
            newBeeMallOrder.setPayType((byte) payType);
            newBeeMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            newBeeMallOrder.setPayTime(Timestamp.valueOf(simpleDateFormat.format(now)));
            newBeeMallOrder.setUpdateTime(Timestamp.valueOf(simpleDateFormat.format(now)));

            if (newBeeMallOrderMapper.updateByPrimaryKeySelective(newBeeMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }

            return ServiceResultEnum.DB_ERROR.getResult();
        }

        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public NewBeeMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        NewBeeMallOrder newBeeMallOrder = newBeeMallOrderMapper.selectByOrderNo(orderNo);
        if (newBeeMallOrder == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!userId.equals(newBeeMallOrder.getUserId())) {
            NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }

        List<NewBeeMallOrderItem> orderItems = newBeeMallOrderItemMapper.selectByOrderId(newBeeMallOrder.getOrderId());
        if (!CollectionUtils.isEmpty(orderItems)) {
            NewBeeMallOrderDetailVO newBeeMallOrderDetailVO = new NewBeeMallOrderDetailVO();
            BeanUtils.copyProperties(newBeeMallOrder, newBeeMallOrderDetailVO);
            newBeeMallOrderDetailVO.setOrderStatusString(NewBeeMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(newBeeMallOrderDetailVO.getOrderStatus()).getName());
            newBeeMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(newBeeMallOrderDetailVO.getPayType()).getName());

            List<NewBeeMallOrderItemVO> newBeeMallOrderItemVOS = BeanUtils.copyList(orderItems, NewBeeMallOrderItemVO.class);
            newBeeMallOrderDetailVO.setNewBeeMallOrderItemVOS(newBeeMallOrderItemVOS);

            return newBeeMallOrderDetailVO;
        }

        NewBeeMallException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
        return null;
    }

    @Override
    public PageResult getMyOrders(PageQueryUtils pageQueryUtils) {
        int total = newBeeMallOrderMapper.getTotalNewBeeMallOrders(pageQueryUtils);

        List<NewBeeMallOrder> newBeeMallOrders = newBeeMallOrderMapper.findNewBeeMallOrderList(pageQueryUtils);
        List<NewBeeMallOrderListVO> newBeeMallOrderListVOS = new ArrayList<>();
        if (total > 0) {
            newBeeMallOrderListVOS = BeanUtils.copyList(newBeeMallOrders, NewBeeMallOrderListVO.class);
            for (NewBeeMallOrderListVO newBeeMallOrderListVO : newBeeMallOrderListVOS) {
                newBeeMallOrderListVO.setOrderStatusString(NewBeeMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(newBeeMallOrderListVO.getOrderStatus()).name());
            }

            List<Long> orderIds = newBeeMallOrders.stream().map(NewBeeMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<NewBeeMallOrderItem> newBeeMallOrderItems = newBeeMallOrderItemMapper.selectByOrderIds(orderIds);

                Map<Long, List<NewBeeMallOrderItem>> itemByOrderIdMap = newBeeMallOrderItems.stream().collect(Collectors.groupingBy(NewBeeMallOrderItem::getOrderId));
                for (NewBeeMallOrderListVO newBeeMallOrderListVO : newBeeMallOrderListVOS) {
                    if (itemByOrderIdMap.containsKey(newBeeMallOrderListVO.getOrderId())) {
                        List<NewBeeMallOrderItem> temp = itemByOrderIdMap.get(newBeeMallOrderListVO.getOrderId());
                        List<NewBeeMallOrderItemVO> newBeeMallOrderListVOList = BeanUtils.copyList(temp, NewBeeMallOrderItemVO.class);
                        newBeeMallOrderListVO.setNewBeeMallOrderItemVOS(newBeeMallOrderListVOList);
                    }
                }
            }
        }

        return new PageResult(newBeeMallOrderListVOS, total, pageQueryUtils.getLimit(), pageQueryUtils.getPage());
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        NewBeeMallOrder newBeeMallOrder = newBeeMallOrderMapper.selectByOrderNo(orderNo);
        if (newBeeMallOrder != null) {
            if (!newBeeMallOrder.getUserId().equals(userId)) {
                NewBeeMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            if (newBeeMallOrder.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                    || newBeeMallOrder.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()
                    || newBeeMallOrder.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                    || newBeeMallOrder.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            if (newBeeMallOrderMapper.closeOrder(Collections.singletonList(newBeeMallOrder.getOrderId()), NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0
                    && recoverStockNum(Collections.singletonList(newBeeMallOrder.getOrderId()))) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }

        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        NewBeeMallOrder newBeeMallOrder = newBeeMallOrderMapper.selectByOrderNo(orderNo);
        if (newBeeMallOrder != null) {
            if (!newBeeMallOrder.getUserId().equals(userId)) {
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            if (newBeeMallOrder.getOrderStatus().intValue() != NewBeeMallOrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            newBeeMallOrder.setOrderStatus((byte) NewBeeMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            newBeeMallOrder.setPayTime(Timestamp.valueOf(simpleDateFormat.format(newBeeMallOrder.getPayTime())));
            newBeeMallOrder.setCreateTime(Timestamp.valueOf(simpleDateFormat.format(newBeeMallOrder.getCreateTime())));
            newBeeMallOrder.setUpdateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
            if (newBeeMallOrderMapper.updateByPrimaryKeySelective(newBeeMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }

            return ServiceResultEnum.DB_ERROR.getResult();
        }

        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    private Boolean recoverStockNum(List<Long> orderIds) {
        List<NewBeeMallOrderItem> newBeeMallOrderItems = newBeeMallOrderItemMapper.selectByOrderIds(orderIds);
        List<GoodsStock> goodsStocks = BeanUtils.copyList(newBeeMallOrderItems, GoodsStock.class);
        int updateStockResult = newBeeMallGoodsMapper.recoverStockNum(goodsStocks);
        if (updateStockResult < 1) {
            NewBeeMallException.fail(ServiceResultEnum.CLOSE_ORDER_ERROR.getResult());
        }

        return Boolean.TRUE;
    }
}
