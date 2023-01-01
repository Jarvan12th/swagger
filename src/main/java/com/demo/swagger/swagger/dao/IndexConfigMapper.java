package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.IndexConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IndexConfigMapper {

    @Results(id = "IndexConfigByTypeAndNumber", value = {
            @Result(property = "configId", column = "config_id"),
            @Result(property = "configName", column = "config_name"),
            @Result(property = "configType", column = "config_type"),
            @Result(property = "goodId", column = "goods_id"),
            @Result(property = "redirectUrl", column = "redirect_url"),
            @Result(property = "carouselRank", column = "config_rank"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "createUser", column = "create_user"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "updateUser", column = "update_user")
    })
    @Select("SELECT * FROM tb_newbee_mall_index_config WHERE config_type = ${configType} AND is_deleted = 0 " +
            "ORDER BY config_rank DESC LIMIT ${number}")
    List<IndexConfig> findIndexConfigByTypeAndNumber(@Param("configType") int configType, @Param("number") int number);
}
