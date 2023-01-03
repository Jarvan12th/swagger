package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.param.SaveCartItemParam;
import com.demo.swagger.swagger.controller.param.UpdateCartItemParam;
import com.demo.swagger.swagger.entity.MallUser;

public interface NewBeeMallShoppingCartService {

    String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    String updateNewBeeMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    String deleteNewBeeMallCartItem(Long cartItemId, MallUser mallUser);
}
