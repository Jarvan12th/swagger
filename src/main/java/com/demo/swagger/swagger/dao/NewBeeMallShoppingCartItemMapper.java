package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.NewBeeMallShoppingCartItem;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewBeeMallShoppingCartItemMapper {

    @Results(id = "NewBeeMallShoppingCartItemByUserIdAndGoodsId", value = {
            @Result(property = "cartItemId", column = "cart_item_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsCount", column = "goods_count"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_shopping_cart_item WHERE user_id = ${userId} AND goods_id = ${goodsId} AND is_deleted = 0")
    NewBeeMallShoppingCartItem selectByUserIdAndGoodsId(Long userId, Long goodsId);

    @Select("SELECT count(*) FROM tb_newbee_mall_shopping_cart_item WHERE user_id = ${userId} and is_deleted = 0")
    int selectCountByUserId(Long userId);

    @Insert("INSERT INTO tb_newbee_mall_shopping_cart_item (user_id, goods_id, goods_count) VALUES (${userId}, ${goodsId}, ${goodsCount})")
    int insertSelective(NewBeeMallShoppingCartItem newBeeMallShoppingCartItem);

    @Results(id = "NewBeeMallShoppingCartItemByPrimaryKey", value = {
            @Result(property = "cartItemId", column = "cart_item_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsCount", column = "goods_count"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_shopping_cart_item WHERE cart_item_id = ${cartItemId} AND is_deleted = 0")
    NewBeeMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    @Update("UPDATE tb_newbee_mall_shopping_cart_item SET goods_count = ${goodsCount}, update_time = '${updateTime}'")
    int updateByPrimaryKeySelective(NewBeeMallShoppingCartItem newBeeMallShoppingCartItem);

    @Update("UPDATE tb_newbee_mall_shopping_cart_item SET is_deleted = 1 WHERE cart_item_id = ${cartItemId} AND is_deleted = 0")
    int deleteByPrimaryKey(Long cartItemId);

    @Results(id = "MyNewBeeCartItems", value = {
            @Result(property = "cartItemId", column = "cart_item_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsCount", column = "goods_count"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_shopping_cart_item WHERE user_id = ${userId} AND is_deleted = 0 limit ${start}, ${limit}")
    List<NewBeeMallShoppingCartItem> findMyNewBeeCartItems(PageQueryUtils pageQueryUtils);

    @Select("SELECT count(*) FROM tb_newbee_mall_shopping_cart_item WHERE user_id = ${userId} AND is_deleted = 0")
    int getTotalMyNewBeeMallCartItems(PageQueryUtils pageQueryUtils);

    @Results(id = "NewBeeCartItemsByUserId", value = {
            @Result(property = "cartItemId", column = "cart_item_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "goodsId", column = "goods_id"),
            @Result(property = "goodsCount", column = "goods_count"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    @Select("SELECT * FROM tb_newbee_mall_shopping_cart_item WHERE user_id = ${userId} AND is_deleted = 0 limit ${number}")
    List<NewBeeMallShoppingCartItem> selectByUserId(Long userId, int number);
}
