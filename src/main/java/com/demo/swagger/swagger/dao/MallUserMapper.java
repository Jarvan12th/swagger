package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.MallUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MallUserMapper {

    @Results(id = "MallUser", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "nickName", column = "nick_name"),
            @Result(property = "loginName", column = "login_name"),
            @Result(property = "passwordMD5", column = "password_md5"),
            @Result(property = "introduceSign", column = "introduce_sign"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "lockedFlag", column = "locked_flag"),
            @Result(property = "createTime", column = "create_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_user WHERE login_name = '${loginName}' AND password_md5 = '${passwordMD5}' AND is_deleted = 0")
    MallUser selectByLoginNameAndPassword(@Param("loginName") String loginName, @Param("passwordMD5") String passwordMD5);

    @Results(id = "MallUserByPrimaryKey", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "nickName", column = "nick_name"),
            @Result(property = "loginName", column = "login_name"),
            @Result(property = "passwordMD5", column = "password_md5"),
            @Result(property = "introduceSign", column = "introduce_sign"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "lockedFlag", column = "locked_flag"),
            @Result(property = "createTime", column = "create_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_user WHERE user_id = '${userId}'")
    MallUser selectByPrimaryKey(@Param("userId") Long userId);

    @Update("UPDATE tb_newbee_mall_user SET nick_name = '${nickName}', password_md5 = '${passwordMD5}', " +
            "introduce_sign = '${introduceSign}' WHERE user_id = '${userId}'")
    int updateByPrimaryKey(MallUser mallUser);
}
