package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.config.annotation.TokenToMallUser;
import com.demo.swagger.swagger.controller.param.SaveCartItemParam;
import com.demo.swagger.swagger.controller.param.UpdateCartItemParam;
import com.demo.swagger.swagger.controller.vo.NewBeeMallShoppingCartItemVO;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallShoppingCartService;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "Cart Api")
@RequestMapping("/api/v1")
public class NewBeeMallShoppingCartController {

    @Resource
    private NewBeeMallShoppingCartService newBeeMallShoppingCartService;

    @ApiOperation(value = "Cart Item List", notes = "Cart Item List")
    @GetMapping("/shop-cart/page")
    public Result<PageResult<List<NewBeeMallShoppingCartItemVO>>> cartItemPageList(Integer pageNumber, @TokenToMallUser MallUser mallUser) {
        Map params = new HashMap(4);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", mallUser.getUserId());
        params.put("page", pageNumber);
        params.put("limit", Constants.SHOPPING_CART_PAGE_LIMIT);

        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        PageResult pageResult = newBeeMallShoppingCartService.getMyShoppingCartItems(pageQueryUtils);
        return ResultGenerator.generateSuccessResult(pageResult);
    }

    @ApiOperation(value = "List Cart Item", notes = "List Cart Item")
    @GetMapping("/shop-cart")
    public Result<List<NewBeeMallShoppingCartItemVO>> cartItemList(@TokenToMallUser MallUser mallUser) {
        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = newBeeMallShoppingCartService.getMyShoppingCartItems(mallUser.getUserId());

        return ResultGenerator.generateSuccessResult(newBeeMallShoppingCartItemVOS);
    }

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

    @ApiOperation(value = "Get Cart Item Detail", notes = "Get Cart Item Detail")
    @GetMapping("/shop-cart/settle")
    public Result<List<NewBeeMallShoppingCartItemVO>> toSettle(Long[] cartItemIds, @TokenToMallUser MallUser mallUser) {
        if (cartItemIds.length < 1) {
            NewBeeMallException.fail("invalid parameters");
        }
        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS =
                newBeeMallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), mallUser.getUserId());

        if (CollectionUtils.isEmpty(newBeeMallShoppingCartItemVOS)) {
            NewBeeMallException.fail("invalid parameters");
        }

        int priceTotal = 0;
        for (NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO : newBeeMallShoppingCartItemVOS) {
            priceTotal += newBeeMallShoppingCartItemVO.getGoodsCount() * newBeeMallShoppingCartItemVO.getSellingPrice();
        }
        if (priceTotal <= 0) {
            NewBeeMallException.fail("price error");
        }

        return ResultGenerator.generateSuccessResult(newBeeMallShoppingCartItemVOS);
    }
}
