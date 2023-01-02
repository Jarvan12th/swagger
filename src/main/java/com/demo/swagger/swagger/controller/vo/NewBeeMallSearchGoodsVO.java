package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewBeeMallSearchGoodsVO implements Serializable {

    @ApiModelProperty("Goods Id")
    private Long goodsId;

    @ApiModelProperty("Goods Name")
    private String goodsName;

    @ApiModelProperty("Goods Introduction")
    private String goodsIntro;

    @ApiModelProperty("Goods Cover Image")
    private String goodsCoverImg;

    @ApiModelProperty("Goods Selling Price")
    private Integer sellingPrice;
}
