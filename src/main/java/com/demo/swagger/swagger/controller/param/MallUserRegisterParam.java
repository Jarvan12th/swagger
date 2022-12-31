package com.demo.swagger.swagger.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MallUserRegisterParam {

    @ApiModelProperty("Login Name")
    @NotEmpty(message = "Login name is required")
    private String loginName;

    @ApiModelProperty("User Password")
    @NotEmpty(message = "Password is required")
    private String password;
}
