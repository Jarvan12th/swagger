package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NewBeeMallIndexCategoryVO implements Serializable {

    @ApiModelProperty("First Category Id")
    private Long categoryId;

    @ApiModelProperty("First Category Level")
    private Byte categoryLevel;

    @ApiModelProperty("First Category Name")
    private String categoryName;

    @ApiModelProperty("Secondary Category List")
    private List<SecondaryLevelCategoryVO> secondaryLevelCategoryVOS;
}
