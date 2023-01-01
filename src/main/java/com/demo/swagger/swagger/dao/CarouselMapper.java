package com.demo.swagger.swagger.dao;

import com.demo.swagger.swagger.entity.Carousel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CarouselMapper {

    @ResultType(Carousel.class)
    @Results(id = "CarouselsByNum", value = {
            @Result(property = "carouselId", column = "carousel_id"),
            @Result(property = "carouselUrl", column = "carousel_url"),
            @Result(property = "redirectUrl", column = "redirect_url"),
            @Result(property = "carouselRank", column = "carousel_rank"),
            @Result(property = "isDeleted", column = "is_deleted"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "createUser", column = "create_user"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "updateUser", column = "update_user")
    })
    @Select("SELECT * from tb_newbee_mall_carousel WHERE is_deleted = 0 ORDER BY carousel_rank DESC LIMIT ${number}")
    List<Carousel> findCarouselsByNum(int number);
}
