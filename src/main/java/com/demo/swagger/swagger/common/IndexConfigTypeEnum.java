package com.demo.swagger.swagger.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;

@AllArgsConstructor
public enum IndexConfigTypeEnum {
    DEFAULT(0, "DEFAULT"),
    INDEX_SEARCH_HOTS(1, "INDEX_SEARCH_HOTS"),
    INDEX_SEARCH_DOWNHOTS(2, "INDEX_SEARCH_DOWNHOTS"),
    INDEX_GOODS_HOT(3, "INDEX_GOODS_HOT"),
    INDEX_GOODS_NEW(4, "INDEX_GOODS_NEW"),
    INDEX_GOODS_RECOMMENDATION(5, "INDEX_GOODS_RECOMMENDATION");

    private int type;

    private String name;

    public static IndexConfigTypeEnum getIndexConfigTypeEnumByType(int type) {
        return Arrays.stream(IndexConfigTypeEnum.values()).filter(i -> i.getType() == type).findFirst().orElse(DEFAULT);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
