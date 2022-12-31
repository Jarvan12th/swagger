package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.controller.param.MallUpdateUserParam;
import com.demo.swagger.swagger.dao.MallUserMapper;
import com.demo.swagger.swagger.dao.MallUserTokenMapper;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.MallUserToken;
import com.demo.swagger.swagger.service.NewBeeMallUserService;
import com.demo.swagger.swagger.utils.MD5Utils;
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
    public boolean updateUserInfo(MallUpdateUserParam mallUpdateUserParam, Long userId) {
        MallUser mallUser = mallUserMapper.selectByPrimaryKey(userId);
        if (mallUser == null) {
            return false;
        }

        mallUser.setNickName(mallUpdateUserParam.getNickName());
        mallUser.setPasswordMD5(mallUpdateUserParam.getPasswordMD5());
        mallUser.setIntroduceSign(mallUpdateUserParam.getIntroduceSign());
        if (mallUserMapper.updateByPrimaryKey(mallUser) > 0) {
            return true;
        }

        return false;
    }

    @Override
    public boolean logout(Long userId) {
        return mallUserTokenMapper.deleteByPrimaryKey(userId) > 0;
    }

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }

        MallUser mallUser = new MallUser();
        mallUser.setLoginName(loginName);
        mallUser.setNickName(loginName);
        String passwordMD5 = MD5Utils.MD5Encode(password);
        mallUser.setPasswordMD5(passwordMD5);
        mallUser.setIntroduceSign(Constants.USER_INTRO);
        mallUser.setIsDeleted(Byte.valueOf("0"));
        mallUser.setLockedFlag(Byte.valueOf("0"));
        mallUser.setCreateTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        if (mallUserMapper.insertSelective(mallUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }

        return ServiceResultEnum.DB_ERROR.getResult();
    }

    private String getNewToken(Long userId) {
        return SystemUtils.generateToken(System.currentTimeMillis() + "" + userId + NumberUtils.generateRandomNumber(4));
    }
}
