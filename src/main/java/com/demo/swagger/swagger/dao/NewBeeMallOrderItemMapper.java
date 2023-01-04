package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.NewBeeMallOrderItem;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface NewBeeMallOrderItemMapper {

    @InsertProvider(type = NewBeeMallOrderItemProviderBuilder.class, method = "buildBatchInsert")
    int batchInsert(@Param("newBeeMallOrderItems") List<NewBeeMallOrderItem> newBeeMallOrderItems);

    class NewBeeMallOrderItemProviderBuilder {
        public static String buildBatchInsert(final List<NewBeeMallOrderItem> newBeeMallOrderItems) {
            return newBeeMallOrderItems.stream().map(o -> getOrderItemSQLStr(o)).collect(Collectors.joining());
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
