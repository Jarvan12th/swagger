package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.config.annotation.TokenToMallUser;
import com.demo.swagger.swagger.controller.param.SaveOrderParam;
import com.demo.swagger.swagger.controller.vo.NewBeeMallOrderDetailVO;
import com.demo.swagger.swagger.controller.vo.NewBeeMallOrderListVO;
import com.demo.swagger.swagger.controller.vo.NewBeeMallShoppingCartItemVO;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.MallUserAddress;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallOrderService;
import com.demo.swagger.swagger.service.NewBeeMallShoppingCartService;
import com.demo.swagger.swagger.service.NewBeeMallUserAddressService;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "Order API")
@RequestMapping("/api/v1")
public class NewBeeMallOrderController {

    @Resource
    private NewBeeMallShoppingCartService newBeeMallShoppingCartService;

    @Resource
    private NewBeeMallOrderService newBeeMallOrderService;

    @Resource
    private NewBeeMallUserAddressService newBeeMallUserAddressService;

    @ApiOperation(value = "Save Order", notes = "Save Order")
    @PostMapping("/saveOrder")
    public Result<String> saveOrder(@ApiParam(value = "Order Params") @RequestBody SaveOrderParam saveOrderParam,
                                    @TokenToMallUser MallUser mallUser) {
        if (saveOrderParam == null || saveOrderParam.getCartItemIds() == null || saveOrderParam.getAddressId() == null) {
            NewBeeMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (saveOrderParam.getCartItemIds().length < 1) {
            NewBeeMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }

        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS =
                newBeeMallShoppingCartService.getCartItemsForSettle(Arrays.asList(saveOrderParam.getCartItemIds()), mallUser.getUserId());
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

        MallUserAddress mallUserAddress = newBeeMallUserAddressService.getMallUserAddressById(saveOrderParam.getAddressId());
        if (!mallUserAddress.getUserId().equals(mallUser.getUserId())) {
            return ResultGenerator.generateFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        String saveOrderResult = newBeeMallOrderService.saveOrder(mallUser, mallUserAddress, newBeeMallShoppingCartItemVOS);

        return ResultGenerator.generateSuccessResult(saveOrderResult);
    }

    @ApiOperation(value = "Mock Success Pay", notes = "Mock Success Pay")
    @GetMapping("/paySuccess")
    public Result paySuccess(@Param("orderNo") @RequestParam("orderNo") String orderNo,
                             @Param("payType") @RequestParam("payType") int payType) {
        String payResult = newBeeMallOrderService.paySuccess(orderNo, payType);

        if (payResult.equals(ServiceResultEnum.SUCCESS.getResult())) {
            return ResultGenerator.generateSuccessResult();
        }

        return ResultGenerator.generateFailResult(payResult);
    }

    @ApiOperation(value = "Get Order Detail", notes = "Get Order Detail")
    @GetMapping("order/{orderNo}")
    public Result<NewBeeMallOrderDetailVO> orderDetailPage(@ApiParam(value = "orderNo") @PathVariable("orderNo") String orderNo,
                                                           @TokenToMallUser MallUser mallUser) {
        NewBeeMallOrderDetailVO newBeeMallOrderDetailVO = newBeeMallOrderService.getOrderDetailByOrderNo(orderNo, mallUser.getUserId());

        return ResultGenerator.generateSuccessResultWithData(newBeeMallOrderDetailVO);
    }

    @ApiOperation(value = "Get Order List", notes = "Get Order List")
    @GetMapping("/order")
    public Result<PageResult<List<NewBeeMallOrderListVO>>> orderList(@ApiParam(value = "pageNumber") @RequestParam(required = false) Integer pageNumber,
                                                                     @ApiParam(value = "orderStatus") @RequestParam(required = false) Integer orderStatus,
                                                                     @TokenToMallUser MallUser mallUser) {
        Map params = new HashMap(4);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }

        params.put("userId", mallUser.getUserId());
        params.put("orderStatus", orderStatus);
        params.put("page", pageNumber);
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);

        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        PageResult pageResult = newBeeMallOrderService.getMyOrders(pageQueryUtils);

        return ResultGenerator.generateSuccessResultWithData(pageResult);
    }
}
