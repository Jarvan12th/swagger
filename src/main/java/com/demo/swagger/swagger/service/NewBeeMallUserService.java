package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.entity.MallUpdateUser;

public interface NewBeeMallUserService {
    String login(String loginName, String passwordMD5);

    boolean updateUserInfo(MallUpdateUser mallUpdateUser, Long userId);

    boolean logout(Long userId);
}
