package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class IndexInfoVO {

    @ApiModelProperty("Carousel List")
    private List<NewBeeMallIndexCarouselVO> carousels;

    @ApiModelProperty("Hot Goods List")
    private List<NewBeeMallIndexConfigGoodsVO> hotGoods;

    @ApiModelProperty("New Goods List")
    private List<NewBeeMallIndexConfigGoodsVO> newGoods;

    @ApiModelProperty("Recommended Goods List")
    private List<NewBeeMallIndexConfigGoodsVO> recommendedGoods;
}
