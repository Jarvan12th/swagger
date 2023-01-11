package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewBeeMallUserAddressVO {

    @ApiModelProperty("Address Id")
    private Long addressId;

    @ApiModelProperty("User Id")
    private Long userId;

    @ApiModelProperty("User Name")
    private String userName;

    @ApiModelProperty("User Phone")
    private String userPhone;

    @ApiModelProperty("Is Default Address")
    private Byte defaultFlag;

    @ApiModelProperty("Province")
    private String provinceName;

    @ApiModelProperty("City")
    private String cityName;

    @ApiModelProperty("Region")
    private String regionName;

    @ApiModelProperty("Address Detail")
    private String detailAddress;
}
