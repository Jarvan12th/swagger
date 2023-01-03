package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.param.SaveCartItemParam;
import com.demo.swagger.swagger.controller.param.UpdateCartItemParam;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;

public interface NewBeeMallShoppingCartService {

    String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    String updateNewBeeMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    String deleteNewBeeMallCartItem(Long cartItemId, MallUser mallUser);

    PageResult getMyShoppingCartItems(PageQueryUtils pageQueryUtils);
}
