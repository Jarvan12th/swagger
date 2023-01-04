package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.MallUserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MallUserAddressMapper {

    @Results(id = "MallUserAddressByPrimaryKey", value = {
            @Result(property = "addressId", column = "address_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "userPhone", column = "user_phone"),
            @Result(property = "defaultFlag", column = "default_flag"),
            @Result(property = "provinceName", column = "province_name"),
            @Result(property = "cityName", column = "city_name"),
            @Result(property = "regionName", column = "region_name"),
            @Result(property = "detailAddress", column = "detail_address"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("SElECT * FROM tb_newbee_mall_user_address WHERE address_id = ${addressId} AND is_deleted = 0")
    MallUserAddress selectByPrimaryKey(Long addressId);

}
