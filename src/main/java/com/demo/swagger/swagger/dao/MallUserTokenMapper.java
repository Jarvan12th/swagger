package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.MallUserToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MallUserTokenMapper {

    @Results(id = "MallUserToken", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "token", column = "token"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "expireTime", column = "expire_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_user_token WHERE user_id = '${userId}'")
    MallUserToken selectByPrimaryKey(Long userId);

    @Insert("INSERT INTO tb_newbee_mall_user_token values ('${userId}', '${token}', '${updateTime}', '${expireTime}')")
    int insertSelective(MallUserToken record);

    @Update("UPDATE tb_newbee_mall_user_token SET token = '${token}', update_time = '${updateTime}', expire_time = '${expireTime}' WHERE user_id = '${userId}'")
    int updateByPrimaryKeySelective(MallUserToken record);
}
