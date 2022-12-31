package com.demo.swagger.swagger.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class MallUserLoginParam implements Serializable {

    @ApiModelProperty("Login Name")
    @NotEmpty(message = "Login name is required")
    private String loginName;

    @ApiModelProperty("Password Md5")
    @NotEmpty(message = "Password is required")
    private String passwordMD5;
}
