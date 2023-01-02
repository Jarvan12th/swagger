package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface GoodsCategoryMapper {

    @Results(id = "GoodsCategoryByLevelAndParentIdsAndNumber", value = {
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "categoryLevel", column = "category_level"),
            @Result(property = "parentId", column = "parent_id"),
            @Result(property = "categoryName", column = "category_name"),
            @Result(property = "categoryRank", column = "category_rank"),
            @Result(property = "isdeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "createUser", column = "create_user"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "updateUser", column = "update_user")
    })
    @SelectProvider(type = GoodsCategoryProviderBuilder.class, method = "buildSelectByLevelAndParentIdsAndNumber")
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int level, int number);

    class GoodsCategoryProviderBuilder {
        public static String buildSelectByLevelAndParentIdsAndNumber(final List<Long> parentIds, final int level, final int number) {
            String parentIdsStr = parentIds.stream().map(Objects::toString).collect(Collectors.joining(","));

            return new SQL() {{
                SELECT("*");
                FROM("tb_newbee_mall_goods_category");
                WHERE("parent_id in (" + parentIdsStr + ") and category_level = " + level + " and is_deleted = 0");
                ORDER_BY("category_rank desc");
                if (number > 0) {
                    LIMIT(number);
                }
            }}.toString();
        }
    }
}
