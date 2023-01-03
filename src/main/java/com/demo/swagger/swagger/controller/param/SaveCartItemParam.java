package com.demo.swagger.swagger.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveCartItemParam implements Serializable {

    @ApiModelProperty("Goods Count")
    private Integer goodsCount;

    @ApiModelProperty(value = "Goods Id")
    private Long goodsId;
}
