package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.NewBeeMallOrderItem;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface NewBeeMallOrderItemMapper {

    @InsertProvider(type = NewBeeMallOrderItemProviderBuilder.class, method = "buildBatchInsert")
    int batchInsert(@Param("newBeeMallOrderItems") List<NewBeeMallOrderItem> newBeeMallOrderItems);

    @Results(id = "NewBeeMallOrderItemByOrderId", value = {
            @Result(property = "orderItemId", column = "order_item_id"),
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsName", column = "goods_name"),
            @Result(property = "goodsCoverImg", column = "goods_cover_img"),
            @Result(property = "sellingPrice", column = "selling_price"),
            @Result(property = "goodsCount", column = "goods_count"),
            @Result(property = "createTime", column = "create_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_order_item WHERE order_id = ${orderId}")
    List<NewBeeMallOrderItem> selectByOrderId(Long orderId);

    @Results(id = "NewBeeMallOrderItemByOrderIds", value = {
            @Result(property = "orderItemId", column = "order_item_id"),
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsName", column = "goods_name"),
            @Result(property = "goodsCoverImg", column = "goods_cover_img"),
            @Result(property = "sellingPrice", column = "selling_price"),
            @Result(property = "goodsCount", column = "goods_count"),
            @Result(property = "createTime", column = "create_time")
    })
    @SelectProvider(value = NewBeeMallOrderItemProviderBuilder.class, method = "buildSelectByOrderList")
    List<NewBeeMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    class NewBeeMallOrderItemProviderBuilder {
        public static String buildBatchInsert(final List<NewBeeMallOrderItem> newBeeMallOrderItems) {
            return newBeeMallOrderItems.stream().map(o -> getOrderItemSQLStr(o)).collect(Collectors.joining());
        }

        public static String buildSelectByOrderList(final List<Long> orderIds) {
            String orderIdsStr = orderIds.stream().map(Object::toString).collect(Collectors.joining(","));

            return new SQL(){{
                SELECT("*");
                FROM("tb_newbee_mall_order_item");
                WHERE("order_id in (" + orderIdsStr + ")");
            }}.toString();
        }

        private static String getOrderItemSQLStr(NewBeeMallOrderItem newBeeMallOrderItem) {
            List<String> list = new ArrayList<>();
            list.add(newBeeMallOrderItem.getOrderId().toString());
            list.add(newBeeMallOrderItem.getGoodsId().toString());
            list.add("'" + newBeeMallOrderItem.getGoodsName() + "'");
            list.add("'" + newBeeMallOrderItem.getGoodsCoverImg() + "'");
            list.add(newBeeMallOrderItem.getSellingPrice().toString());
            list.add(newBeeMallOrderItem.getGoodsCount().toString());
            String values = list.stream().collect(Collectors.joining(","));

            return new SQL(){{
                INSERT_INTO("tb_newbee_mall_order_item (order_id, goods_id, goods_name, goods_cover_img, selling_price, goods_count)");
                INTO_VALUES(values);
            }}.toString() + ";";
        }
    }
}
