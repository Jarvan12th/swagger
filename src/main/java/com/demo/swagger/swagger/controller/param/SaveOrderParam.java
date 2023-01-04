package com.demo.swagger.swagger.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveOrderParam implements Serializable {

    @ApiModelProperty("Cart Item Id List")
    private Long[] cartItemIds;

    @ApiModelProperty("Address Id")
    private Long addressId;
}
