package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewBeeMallShoppingCartItemVO {

    @ApiModelProperty("Cart Item Id")
    private Long cartItemId;

    @ApiModelProperty("Goods Id")
    private Long goodsId;

    @ApiModelProperty("Goods Count")
    private Integer goodsCount;

    @ApiModelProperty("Goods Name")
    private String goodsName;

    @ApiModelProperty("Goods Cover Image")
    private String goodsCoverImg;

    @ApiModelProperty("Goods Selling Price")
    private Integer sellingPrice;
}
