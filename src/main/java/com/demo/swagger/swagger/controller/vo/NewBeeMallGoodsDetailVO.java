package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewBeeMallGoodsDetailVO implements Serializable {

    @ApiModelProperty("Goods Id")
    private Long goodsId;

    @ApiModelProperty("Goods Name")
    private String goodsName;

    @ApiModelProperty("Goods Introduction")
    private String goodsIntro;

    @ApiModelProperty("Goods Cover Image")
    private String goodsCoverImg;

    @ApiModelProperty("Goods Selling price")
    private Integer sellingPrice;

    @ApiModelProperty("Goods Tag")
    private String tag;

    @ApiModelProperty("Goods Carousel List")
    private String[] goodsCarouselList;

    @ApiModelProperty("Goods Original Price")
    private Integer originalPrice;

    @ApiModelProperty("Goods Detail Content")
    private String goodsDetailContent;
}
