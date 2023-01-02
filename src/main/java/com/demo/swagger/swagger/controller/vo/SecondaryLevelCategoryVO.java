package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SecondaryLevelCategoryVO implements Serializable {

    @ApiModelProperty("Secondary Category Id")
    private Long categoryId;

    @ApiModelProperty("Parent Category Id")
    private Long parentId;

    @ApiModelProperty("Secondary Category Level")
    private Byte categoryLevel;

    @ApiModelProperty("Secondary Category Name")
    private String categoryName;

    @ApiModelProperty("Third Category List")
    private List<ThirdLevelCategoryVO> thirdLevelCategoryVOS;
}
