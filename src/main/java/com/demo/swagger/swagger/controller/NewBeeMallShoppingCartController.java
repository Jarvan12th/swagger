package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.config.annotation.TokenToMallUser;
import com.demo.swagger.swagger.controller.param.SaveCartItemParam;
import com.demo.swagger.swagger.controller.param.UpdateCartItemParam;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.NewBeeMallShoppingCartItem;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallShoppingCartService;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "v1", tags = "Cart Api")
@RequestMapping("/api/v1")
public class NewBeeMallShoppingCartController {

    @Resource
    private NewBeeMallShoppingCartService newBeeMallShoppingCartService;

    @ApiOperation(value = "Save Goods to Cart", notes = "Save Goods to Cart")
    @PostMapping("/shop-cart")
    public Result saveNewBeeMallShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                                 @TokenToMallUser MallUser mallUser) {
        String saveResult = newBeeMallShoppingCartService.saveNewBeeMallCartItem(saveCartItemParam, mallUser.getUserId());

        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.generateSuccessResult();
        }

        return ResultGenerator.generateFailResult(saveResult);
    }

    @ApiOperation(value = "Update Cart Goods", notes = "Update Cart Goods")
    @PutMapping("/shop-cart")
    public Result updateNewBeeMallShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                                   @TokenToMallUser MallUser mallUser) {
        String updateResult = newBeeMallShoppingCartService.updateNewBeeMallCartItem(updateCartItemParam, mallUser.getUserId());

        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.generateSuccessResult();
        }

        return ResultGenerator.generateFailResult(updateResult);
    }

    @ApiOperation(value = "Delete Cart Item", notes = "Delete Cart Item")
    @DeleteMapping("/shop-cart/{cartItemId}")
    public Result deleteNewBeeMallShoppingCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                   @TokenToMallUser MallUser mallUser) {
        String deleteResult = newBeeMallShoppingCartService.deleteNewBeeMallCartItem(cartItemId, mallUser);
        if (ServiceResultEnum.SUCCESS.getResult().equals(deleteResult)) {
            return ResultGenerator.generateSuccessResult();
        }

        return ResultGenerator.generateFailResult(deleteResult);
    }
}
