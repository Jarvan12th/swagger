package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.NewBeeMallOrder;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

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

    @SelectProvider(type = OrderProviderBuilder.class, method = "buildGetTotalNewBeeMallOrders")
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
    @SelectProvider(type = OrderProviderBuilder.class, method = "buildFindNewBeeMallOrderList")
    List<NewBeeMallOrder> findNewBeeMallOrderList(PageQueryUtils pageQueryUtils);

    @UpdateProvider(type = OrderProviderBuilder.class, method = "buildCloseOrder")
    int closeOrder(List<Long> orderIds, int orderStatus);

    class OrderProviderBuilder {
        public static String buildGetTotalNewBeeMallOrders(final PageQueryUtils pageQueryUtils) {
            String whereClause = "user_id = ${userId}";
            if (pageQueryUtils.get("orderStatus") != null) {
                whereClause +=  " AND order_status = " + pageQueryUtils.get("orderStatus");
            }

            String finalWhereClause = whereClause;
            return new SQL(){{
                SELECT("count(*)");
                FROM("tb_newbee_mall_order");
                WHERE(finalWhereClause);
            }}.toString();
        }

        public static String buildFindNewBeeMallOrderList(final PageQueryUtils pageQueryUtils) {
            String whereClause = "user_id = ${userId}";
            if (pageQueryUtils.get("orderStatus") != null) {
                whereClause +=  " AND order_status = " + pageQueryUtils.get("orderStatus");
            }

            String finalWhereClause = whereClause;
            return new SQL(){{
                SELECT("*");
                FROM("tb_newbee_mall_order");
                WHERE(finalWhereClause);
                ORDER_BY("create_time DESC LIMIT ${start}, ${limit}");
            }}.toString();
        }

        public static String buildCloseOrder(final List<Long> orderIds, final int orderStatus) {
            String orderIdsStr = orderIds.stream().map(Object::toString).collect(Collectors.joining(","));
            return new SQL(){{
                UPDATE("tb_newbee_mall_order");
                SET("order_status = " + orderStatus + ", update_time = now()");
                WHERE("order_id in (" + orderIdsStr + ")");
            }}.toString();
        }
    }
}
