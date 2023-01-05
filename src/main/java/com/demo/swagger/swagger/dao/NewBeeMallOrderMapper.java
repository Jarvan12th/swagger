package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.NewBeeMallOrder;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewBeeMallOrderMapper {

    @Insert("INSERT INTO tb_newbee_mall_order (order_no, user_id, total_price, extra_info) " +
            "VALUES ('${orderNo}', ${userId}, ${totalPrice}, '${extraInfo}')")
    Long insertSelective(NewBeeMallOrder newBeeMallOrder);

    @Select("SELECT LAST_INSERT_ID()")
    Long selectLastInsertAutoIncrementId();

    @Results(id = "NewBeeMallOrderByOrderNo", value = {
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "orderNo", column = "order_no"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "totalPrice", column = "total_price"),
            @Result(property = "payStatus", column = "pay_status"),
            @Result(property = "payType", column = "pay_type"),
            @Result(property = "payTime", column = "pay_time"),
            @Result(property = "orderStatus", column = "order_status"),
            @Result(property = "extraInfo", column = "extra_info"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_order WHERE order_no = '${orderNo}' AND is_deleted = 0 LIMIT 1")
    NewBeeMallOrder selectByOrderNo(String orderNo);

    @Update("UPDATE tb_newbee_mall_order SET pay_status = ${payStatus}, pay_type = ${payType}, pay_time = '${payTime}', " +
            "order_status = ${orderStatus}, update_time = '${updateTime}' WHERE order_id = ${orderId}")
    int updateByPrimaryKeySelective(NewBeeMallOrder newBeeMallOrder);

    @Select("SELECT count(*) FROM tb_newbee_mall_order WHERE user_id = ${userId} AND order_status = ${orderStatus}")
    int getTotalNewBeeMallOrders(PageQueryUtils pageQueryUtils);

    @Results(id = "NewBeeMallOrderList", value = {
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "orderNo", column = "order_no"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "totalPrice", column = "total_price"),
            @Result(property = "payStatus", column = "pay_status"),
            @Result(property = "payType", column = "pay_type"),
            @Result(property = "payTime", column = "pay_time"),
            @Result(property = "orderStatus", column = "order_status"),
            @Result(property = "extraInfo", column = "extra_info"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_order WHERE user_id = ${userId} AND order_status = ${orderStatus} " +
            "ORDER BY create_time DESC LIMIT ${start}, ${limit}")
    List<NewBeeMallOrder> findNewBeeMallOrderList(PageQueryUtils pageQueryUtils);
}
