package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewBeeMallUserVO {

    @ApiModelProperty("User Nick Name")
    private String nickName;

    @ApiModelProperty("User Login Name")
    private String loginName;

    @ApiModelProperty("User Introduce Sign")
    private String introduceSign;
}
