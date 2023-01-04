package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.NewBeeMallOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NewBeeMallOrderMapper {

    @Insert("INSERT INTO tb_newbee_mall_order (order_no, user_id, total_price, extra_info) " +
            "VALUES ('${orderNo}', ${userId}, ${totalPrice}, '${extraInfo}')")
    Long insertSelective(NewBeeMallOrder newBeeMallOrder);

    @Select("SELECT LAST_INSERT_ID()")
    Long selectLastInsertAutoIncrementId();
}
