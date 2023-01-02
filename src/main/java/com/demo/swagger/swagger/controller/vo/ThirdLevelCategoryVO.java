package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdLevelCategoryVO implements Serializable {

    @ApiModelProperty("Third Category Id")
    private Long categoryId;

    @ApiModelProperty("Third Category Level")
    private Byte categoryLevel;

    @ApiModelProperty("Third Category Name")
    private String categoryName;
}
