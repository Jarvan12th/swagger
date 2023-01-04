package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.NewBeeMallOrderAddress;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewBeeMallOrderAddressMapper {

    @Insert("INSERT INTO tb_newbee_mall_order_address VALUES " +
            "(${orderId}, '${userName}', '${userPhone}', '${provinceName}', '${cityName}', '${regionName}', '${detailAddress}')")
    int insertSelective(NewBeeMallOrderAddress newBeeMallOrderAddress);
}
