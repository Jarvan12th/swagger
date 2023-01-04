package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.GoodsStock;
import com.demo.swagger.swagger.entity.NewBeeMallGoods;
import com.demo.swagger.swagger.utils.PageQueryUtils;
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

    @Results(id = "GoodsListBySearch", value = {
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
    @SelectProvider(type = GoodsProviderBuilder.class, method = "buildFindNewBeeMallGoodsListBySearch")
    List<NewBeeMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtils pageQueryUtils);

    @SelectProvider(type = GoodsProviderBuilder.class, method = "buildGetTotalNewBeeMallGoodsBySearch")
    int getTotalNewBeeMallGoodsBySearch(PageQueryUtils pageQueryUtils);

    @Results(id = "GoodsByPrimaryKey", value = {
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
    @Select("SELECT * FROM tb_newbee_mall_goods_info WHERE goods_id = ${goodsId}")
    NewBeeMallGoods selectByPrimaryKey(Long goodsId);

    @UpdateProvider(type = GoodsProviderBuilder.class, method = "buildUpdateGoodsStock")
    int updateGoodsStock(@Param("goodsStocks") List<GoodsStock> goodsStocks);

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

        public static String buildFindNewBeeMallGoodsListBySearch(final PageQueryUtils pageQueryUtils) {
            String whereClause = getWhereClause(pageQueryUtils);

            String orderByClause = getOrderByClause(pageQueryUtils);

            String limitClause = getLimitClause(pageQueryUtils);

            return new SQL(){{
                SELECT("*");
                FROM("tb_newbee_mall_goods_info");
                if (!"".equals(whereClause)) {
                    WHERE(whereClause);
                }
                if (!"".equals(orderByClause)) {
                    ORDER_BY(orderByClause);
                }
                if (!"".equals(limitClause)) {
                    LIMIT(limitClause);
                }
            }}.toString();
        }

        public static String buildGetTotalNewBeeMallGoodsBySearch(final PageQueryUtils pageQueryUtils) {
            String whereClause = getWhereClause(pageQueryUtils);

            return new SQL(){{
                SELECT("count(*)");
                FROM("tb_newbee_mall_goods_info");
                if (!"".equals(whereClause)) {
                    WHERE(whereClause);
                }
            }}.toString();
        }

        public static String buildUpdateGoodsStock(final List<GoodsStock> goodsStocks) {
            StringBuilder sb = new StringBuilder();
            for (GoodsStock goodsStock : goodsStocks) {
                sb.append(new SQL(){{
                    UPDATE("tb_newbee_mall_goods_info");
                    SET("stock_num = stock_num - " + goodsStock.getGoodsCount());
                    WHERE("goods_id = " + goodsStock.getGoodsId() +
                            " AND stock_num >= " + goodsStock.getGoodsCount() + " AND goods_sell_status = 0");
                }});
                sb.append(";");
            }

            return sb.toString();
        }

        private static String getLimitClause(PageQueryUtils pageQueryUtils) {
            StringBuilder sb = new StringBuilder();
            int start = (int) pageQueryUtils.get("start");
            int limit = (int) pageQueryUtils.get("limit");
            sb.append(start + ", " + limit);

            return sb.toString();
        }

        private static String getOrderByClause(PageQueryUtils pageQueryUtils) {
            StringBuilder sb = new StringBuilder();
            String orderBy = (String) pageQueryUtils.get("orderBy");
            if (orderBy != null) {
                if ("new" == orderBy) {
                    sb.append("order by goods_id desc");
                } else if ("price" == orderBy) {
                    sb.append("order by selling_price asc");
                } else {
                    sb.append("order by stock_num desc");
                }
            }

            return sb.toString();
        }

        private static String getWhereClause(PageQueryUtils pageQueryUtils) {
            StringBuilder sb = new StringBuilder();
            String keyword = (String) pageQueryUtils.get("keyword");
            if (keyword != null && keyword != "") {
                sb.append("(goods_name like '%" + keyword + "%' or goods_intro like '%" + keyword + "%') and ");
            }
            Long goodsCategoryId = (Long) pageQueryUtils.get("goodsCategoryId");
            if (goodsCategoryId != null) {
                sb.append("goods_category_id = " + goodsCategoryId + " and ");
            }
            Byte goodsSellStatus = (Byte) pageQueryUtils.get("goodsSellStatus");
            if (goodsSellStatus != null) {
                sb.append("goods_sell_status = " + goodsSellStatus + " and ");
            }

            return sb.length() > 4 ? sb.subSequence(0, sb.length() - 4).toString() : sb.toString();
        }
    }
}
