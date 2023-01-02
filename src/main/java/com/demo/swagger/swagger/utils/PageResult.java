package com.demo.swagger.swagger.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    @ApiModelProperty("Total Records count")
    private int totalCount;

    @ApiModelProperty("Size Per Page")
    private int pageSize;

    @ApiModelProperty("Total Pages Count")
    private int totalPage;

    @ApiModelProperty("Current Page Number")
    private int currPage;

    @ApiModelProperty("List Data")
    private List<T> list;

    public PageResult(List<T> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }
}
