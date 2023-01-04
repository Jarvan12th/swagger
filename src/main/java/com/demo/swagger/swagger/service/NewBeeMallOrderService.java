package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.vo.NewBeeMallShoppingCartItemVO;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.MallUserAddress;

import java.util.List;

public interface NewBeeMallOrderService {

    String saveOrder(MallUser mallUser, MallUserAddress mallUserAddress, List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS);
}
