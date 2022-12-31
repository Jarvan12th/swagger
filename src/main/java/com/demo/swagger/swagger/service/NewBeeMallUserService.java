package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.param.MallUpdateUserParam;

public interface NewBeeMallUserService {
    String login(String loginName, String passwordMD5);

    boolean updateUserInfo(MallUpdateUserParam mallUpdateUserParam, Long userId);

    boolean logout(Long userId);

    String register(String loginName, String password);
}
