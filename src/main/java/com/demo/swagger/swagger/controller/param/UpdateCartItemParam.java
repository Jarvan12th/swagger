package com.demo.swagger.swagger.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateCartItemParam implements Serializable {

    @ApiModelProperty("Cart Item Id")
    private Long cartItemId;

    @ApiModelProperty("Goods Count")
    private Integer goodsCount;
}
