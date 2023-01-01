package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewBeeMallIndexConfigGoodsVO {

    @ApiModelProperty("Good Id")
    private Long goodsId;

    @ApiModelProperty("Good Name")
    private String goodsName;

    @ApiModelProperty("Good Cover Image Url")
    private String goodsCoverImg;

    @ApiModelProperty("Selling Price")
    private Integer sellingPrice;
}
