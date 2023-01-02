package com.demo.swagger.swagger.common;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum NewBeeMallCategoryLevelEnum {
    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "LEVEL ONE"),
    LEVEL_TWO(2, "LEVEL TWO"),
    LEVEL_THREE(3, "LEVEL THREE");

    private int level;

    private String name;

    public static NewBeeMallCategoryLevelEnum getNewBeeMallOrderStatusEnumByLevel(int level) {
        return Arrays.stream(NewBeeMallCategoryLevelEnum.values())
                .filter(l -> l.getLevel() == level)
                .findFirst()
                .orElse(DEFAULT);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
