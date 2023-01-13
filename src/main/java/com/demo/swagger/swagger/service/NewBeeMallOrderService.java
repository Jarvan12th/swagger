package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.vo.NewBeeMallOrderDetailVO;
import com.demo.swagger.swagger.controller.vo.NewBeeMallShoppingCartItemVO;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.MallUserAddress;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;

import java.util.List;

public interface NewBeeMallOrderService {

    String saveOrder(MallUser mallUser, MallUserAddress mallUserAddress, List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS);

    String paySuccess(String orderNo, int payType);

    NewBeeMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    PageResult getMyOrders(PageQueryUtils pageQueryUtils);

    String cancelOrder(String orderNo, Long userId);

    String finishOrder(String orderNo, Long userId);
}
