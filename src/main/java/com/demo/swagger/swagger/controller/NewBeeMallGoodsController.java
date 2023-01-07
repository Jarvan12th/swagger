package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.config.annotation.TokenToMallUser;
import com.demo.swagger.swagger.controller.vo.NewBeeMallGoodsDetailVO;
import com.demo.swagger.swagger.controller.vo.NewBeeMallSearchGoodsVO;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.NewBeeMallGoods;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallGoodsService;
import com.demo.swagger.swagger.utils.BeanUtils;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "Goods Api")
@RequestMapping("/api/v1")
@Slf4j
public class NewBeeMallGoodsController {

    @Resource
    private NewBeeMallGoodsService newBeeMallGoodsService;

    @ApiOperation(value = "Search Goods", notes = "Search Goods")
    @GetMapping("/search")
    public Result<PageResult<List<NewBeeMallSearchGoodsVO>>> search (
            @RequestParam(required = false) @ApiParam(value = "keyword") String keyword,
            @RequestParam(required = false) @ApiParam(value = "categoryId") Long goodsCategoryId,
            @RequestParam(required = false) @ApiParam(value = "orderBy") String orderBy,
            @RequestParam(required = false) @ApiParam(value = "pageNumber") Integer pageNumber,
            @TokenToMallUser MallUser mallUser
            ) {
        log.info("Goods search api, keyword = {}, goodsCategoryId = {}, orderBy = {}, pageNumber = {}, userId = {}", keyword, goodsCategoryId, orderBy, pageNumber, mallUser.getUserId());

        Map params = new HashMap(4);
        if (goodsCategoryId == null && StringUtils.isEmpty(keyword)) {
            NewBeeMallException.fail("Illegal parameters");
        }
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }

        params.put("goodsCategoryId", goodsCategoryId);
        params.put("page", pageNumber);
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);

        if (!StringUtils.isEmpty(keyword)) {
            params.put("keyword", keyword);
        }

        if (!StringUtils.isEmpty(orderBy)) {
            params.put("orderBy", orderBy);
        }

        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);

        PageQueryUtils pageQueryUtils = new PageQueryUtils(params);
        PageResult pageResult = newBeeMallGoodsService.searchNewBeeMallGoods(pageQueryUtils);

        return ResultGenerator.generateSuccessResultWithData(pageResult);
    }

    @ApiOperation(value = "Good Detail Api", notes = "Get Goods Detail")
    @GetMapping("/goods/detail/{goodsId}")
    public Result<NewBeeMallGoodsDetailVO> goodsDetail(@ApiParam(value = "Goods Id") @PathVariable("goodsId") Long goodsId, @TokenToMallUser MallUser mallUser) {
        log.info("Goods detail api, goodsId = {}, userId = {}", goodsId, mallUser.getUserId());

        if (goodsId < 1) {
            return ResultGenerator.generateFailResult("Invalid Parameter");
        }

        NewBeeMallGoods newBeeMallGoods = newBeeMallGoodsService.getNewBeeMallGoodsById(goodsId);
        if (newBeeMallGoods == null) {
            return ResultGenerator.generateFailResult("No such goods");
        }

        if (Constants.SELL_STATUS_UP != newBeeMallGoods.getGoodsSellStatus()) {
            NewBeeMallException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }

        NewBeeMallGoodsDetailVO newBeeMallGoodsDetailVO = new NewBeeMallGoodsDetailVO();
        BeanUtils.copyProperties(newBeeMallGoods, newBeeMallGoodsDetailVO);
        newBeeMallGoodsDetailVO.setGoodsCarouselList(newBeeMallGoods.getGoodsCarousel().split(","));

        return ResultGenerator.generateSuccessResultWithData(newBeeMallGoodsDetailVO);
    }
}
