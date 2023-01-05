package com.demo.swagger.swagger.common;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum PayStatusEnum {

    DEFAULT(-1, "FAIL"),
    PAY_ING(0, "PAYING"),
    PAY_SUCCESS(1, "SUCCESS");

    private int payStatus;

    private String name;

    public static PayStatusEnum getPayStatusEnumByStatus(int payStatus) {
        return Arrays.stream(PayStatusEnum.values()).filter(status -> status.getPayStatus() == payStatus).findFirst().orElse(DEFAULT);
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
