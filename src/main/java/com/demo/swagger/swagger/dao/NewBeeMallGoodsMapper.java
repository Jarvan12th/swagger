package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.NewBeeMallGoods;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface NewBeeMallGoodsMapper {

    @Results(id = "GoodsByPrimaryKeys", value = {
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsName", column = "goods_name"),
            @Result(property = "goodsIntro", column = "goods_intro"),
            @Result(property = "goodsCategoryId", column = "goods_category_id"),
            @Result(property = "goodsCoverImg", column = "goods_cover_img"),
            @Result(property = "goodsCarousel", column = "goods_carousel"),
            @Result(property = "goodsDetailContent", column = "goods_detail_content"),
            @Result(property = "originalPrice", column = "original_price"),
            @Result(property = "sellingPrice", column = "selling_price"),
            @Result(property = "stockNum", column = "stock_num"),
            @Result(property = "tag", column = "tag"),
            @Result(property = "goodsSellStatus", column = "goods_sell_status"),
            @Result(property = "createUser", column = "create_user"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateUser", column = "update_user"),
            @Result(property = "updateTime", column = "update_time")
    })
    @SelectProvider(type = GoodsProviderBuilder.class, method = "buildSelectGoodsByPrimaryKeys")
    List<NewBeeMallGoods> selectGoodsByPrimaryKeys(@Param("goodIds") List<Long> goodIds);

    class GoodsProviderBuilder {
        public static String buildSelectGoodsByPrimaryKeys(final List<Long> goodIds) {
            String goodIdsStr = goodIds.stream().map(Object::toString).collect(Collectors.joining(","));
            return new SQL(){{
                SELECT("*");
                FROM("tb_newbee_mall_goods_info");
                WHERE("goods_id in (" + goodIdsStr + ")");
                ORDER_BY("field(goods_id, " + goodIdsStr + ")");
            }}.toString();
        }
    }
}
