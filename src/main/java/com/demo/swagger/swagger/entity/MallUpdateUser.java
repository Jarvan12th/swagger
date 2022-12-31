package com.demo.swagger.swagger.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MallUpdateUser implements Serializable {

    @ApiModelProperty("User Nick Name")
    private String nickName;

    @ApiModelProperty("User Password")
    private String passwordMD5;

    @ApiModelProperty("User Introduce Sign")
    private String introduceSign;
}
