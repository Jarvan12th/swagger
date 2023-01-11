package com.demo.swagger.swagger.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveMallUserAddressParam {

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
