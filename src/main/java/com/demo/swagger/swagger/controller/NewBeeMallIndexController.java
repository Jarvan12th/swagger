package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.common.IndexConfigTypeEnum;
import com.demo.swagger.swagger.controller.vo.IndexInfoVO;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallCarouselService;
import com.demo.swagger.swagger.service.NewBeeMallIndexConfigService;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(value = "v1", tags = "Index API")
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", allowCredentials = "true")
public class NewBeeMallIndexController {

    @Resource
    private NewBeeMallCarouselService newbeeMallCarouselService;

    @Resource
    private NewBeeMallIndexConfigService newBeeMallIndexConfigService;

    @ApiOperation(value = "Get Index Data", notes = "Get Carousel/NewGoods/Recommendation data")
    @GetMapping("/index/infos")
    public Result<IndexInfoVO> indexInfo() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        indexInfoVO.setCarousels(newbeeMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER));
        indexInfoVO.setHotGoods(newBeeMallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER));
        indexInfoVO.setNewGoods(newBeeMallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER));
        indexInfoVO.setRecommendedGoods(newBeeMallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMENDATION.getType(), Constants.INDEX_GOODS_RECOMMENDATION_NUMBER));

        return ResultGenerator.generateSuccessResultWithData(indexInfoVO);
    }
}
