package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.config.annotation.TokenToMallUser;
import com.demo.swagger.swagger.controller.param.MallUserLoginParam;
import com.demo.swagger.swagger.controller.vo.NewBeeMallUserVO;
import com.demo.swagger.swagger.entity.MallUpdateUser;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallUserService;
import com.demo.swagger.swagger.utils.BeanUtils;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "test1", notes = "test @TokenToMallUser")
    @GetMapping("/test1")
    public Result<String> test1(@TokenToMallUser MallUser mallUser) {
        if (mallUser == null) {
            return ResultGenerator.generateErrorResult(416, "Have not Logged in in yet");
        } else {
            return ResultGenerator.generateSuccessResult("Login passed");
        }
    }

    @ApiOperation(value = "test2", notes = "test no @TokenToMallUser")
    @GetMapping("/test2")
    public Result<String> test2() {
        return ResultGenerator.generateSuccessResult("Login passed");
    }

    @ApiOperation(value = "Get User Detail", notes = "Get User Detail")
    @GetMapping("/user/info")
    public Result<NewBeeMallUserVO> getUserDetail(@TokenToMallUser MallUser mallUser) {
        NewBeeMallUserVO newBeeMallUserVO = new NewBeeMallUserVO();
        BeanUtils.copyProperties(mallUser, newBeeMallUserVO);

        return ResultGenerator.generateSuccessResult(newBeeMallUserVO);
    }

    @ApiOperation(value = "Update User Detail", notes = "Update User Detail")
    @PutMapping("/user/info")
    public Result<String> updateUserDetail(@RequestBody @ApiParam("Update User Info") MallUpdateUser mallUpdateUser,
                                           @TokenToMallUser MallUser mallUser) {
        boolean flag = newBeeMallUserService.updateUserInfo(mallUpdateUser, mallUser.getUserId());

        if (flag) {
            return ResultGenerator.generateSuccessResult();
        } else {
            return ResultGenerator.generateFailResult("Update Fail");
        }
    }
}
