package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.controller.param.MallUserLoginParam;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallUserService;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(value = "v1", tags = "NewBee Mall User Related Api")
@RequestMapping("/api/v1")
@Slf4j
public class NewBeeMallUserController {

    @Resource
    private NewBeeMallUserService newBeeMallUserService;

    @ApiOperation(value = "login api", notes = "return token")
    @PostMapping("/user/login")
    public Result<String> login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
        String loginResult = newBeeMallUserService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMD5());

        log.info("login api, loginName = {}, loginResult = {}", mallUserLoginParam.getLoginName(), loginResult);

        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            return ResultGenerator.generateSuccessResult(loginResult);
        }

        return ResultGenerator.generateFailResult(loginResult);
    }
}
