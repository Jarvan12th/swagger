package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.dao.MallUserMapper;
import com.demo.swagger.swagger.dao.MallUserTokenMapper;
import com.demo.swagger.swagger.entity.MallUpdateUser;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.MallUserToken;
import com.demo.swagger.swagger.service.NewBeeMallUserService;
import com.demo.swagger.swagger.utils.NumberUtils;
import com.demo.swagger.swagger.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class NewBeeMallUserServiceImpl implements NewBeeMallUserService {

    @Autowired
    private MallUserMapper mallUserMapper;

    @Autowired
    private MallUserTokenMapper mallUserTokenMapper;

    @Override
    public String login(String loginName, String passwordMD5) {
        MallUser user = mallUserMapper.selectByLoginNameAndPassword(loginName, passwordMD5);

        if (user != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }

            String token = getNewToken(user.getUserId());
            MallUserToken mallUserToken = mallUserTokenMapper.selectByPrimaryKey(user.getUserId());

            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            Date nowSqlDate = Timestamp.valueOf(simpleDate.format(now));
            Date expireTime = new Date(now.getTime() + 2 * 24 * 60 * 60 * 1000);
            Date expireTimeSqlDate = Timestamp.valueOf(simpleDate.format(expireTime));

            if (mallUserToken == null) {
                mallUserToken = new MallUserToken(user.getUserId(), token, nowSqlDate, expireTimeSqlDate);
                if (mallUserTokenMapper.insertSelective(mallUserToken) > 0) {
                    return token;
                }
            } else {
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(nowSqlDate);
                mallUserToken.setExpireTime(expireTimeSqlDate);

                if (mallUserTokenMapper.updateByPrimaryKeySelective(mallUserToken) > 0) {
                    return token;
                }
            }
        }

        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public boolean updateUserInfo(MallUpdateUser mallUpdateUser, Long userId) {
        MallUser mallUser = mallUserMapper.selectByPrimaryKey(userId);
        if (mallUser == null) {
            return false;
        }

        mallUser.setNickName(mallUpdateUser.getNickName());
        mallUser.setPasswordMD5(mallUpdateUser.getPasswordMD5());
        mallUser.setIntroduceSign(mallUpdateUser.getIntroduceSign());
        if (mallUserMapper.updateByPrimaryKey(mallUser) > 0) {
            return true;
        }

        return false;
    }

    private String getNewToken(Long userId) {
        return SystemUtils.generateToken(System.currentTimeMillis() + "" + userId + NumberUtils.generateRandomNumber(4));
    }
}
