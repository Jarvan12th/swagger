package com.demo.swagger.swagger.utils;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class PageQueryUtils extends LinkedHashMap<String, Object> {

    private int page;

    private int limit;

    public PageQueryUtils(Map<String, Object> params) {
        this.putAll(params);
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("start", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
    }

    @Override
    public String toString() {
        return "PageUtils{" + "page=" + page + ", limit" + limit + "}";
    }
}
