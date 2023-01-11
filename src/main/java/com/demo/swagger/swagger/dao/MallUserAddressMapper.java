package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.MallUserAddress;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Results(id = "MallUserAddressByUserId", value = {
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
    @Select("SELECT * FROM tb_newbee_mall_user_address WHERE user_id = ${userId} AND is_deleted = 0 ORDER BY address_id desc limit 20")
    List<MallUserAddress> findMyAddressList(Long userId);

    @Results(id = "MallUserAddressByDefault", value = {
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
    @Select("SELECT * FROM tb_newbee_mall_user_address WHERE user_id = ${userId} AND default_flag = 1 AND is_deleted = 0 LIMIT 1")
    MallUserAddress getMyDefaultAddress(Long userId);

    @Update("UPDATE tb_newbee_mall_user_address SET user_name = '${userName}', " +
            "user_phone = '${userPhone}', default_flag = ${defaultFlag}, province_name = '${provinceName}', city_name = '${cityName}', " +
            "region_name = '${regionName}', detail_address = '${detailAddress}', is_deleted = ${isDeleted}, update_time = '${updateTime}' " +
            "WHERE address_id = ${addressId}")
    int updateByPrimaryKeySelective(MallUserAddress mallUserAddress);

    @Insert("INSERT INTO tb_newbee_mall_user_address VALUES (null, ${userId}, '${userName}', '${userPhone}', ${defaultFlag}, " +
            "'${provinceName}', '${cityName}', '${regionName}', '${detailAddress}', 0, now(), now())")
    int insertSelective(MallUserAddress mallUserAddress);

    @Update("UPDATE tb_newbee_mall_user_address SET is_deleted = 1 WHERE address_id = ${addressId}")
    int deleteByPrimaryKey(Long addressId);
}
