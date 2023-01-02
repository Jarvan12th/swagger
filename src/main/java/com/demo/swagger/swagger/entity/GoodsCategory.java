package com.demo.swagger.swagger.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class GoodsCategory {

    private Long categoryId;

    private Byte categoryLevel;

    private Long parentId;

    private String categoryName;

    private Integer categoryRank;

    private Byte isdeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private Date createTime;

    private Integer createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private Date updateTime;

    private Integer updateUser;
}
