package com.demo.swagger.swagger.common;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum NewBeeMallOrderStatusEnum {

    DEFAULT(-9, "ERROR"),
    ORDER_PRE_PAY(0, "PRE_PAY"),
    ORDER_PAID(1, "PAID"),
    ORDER_PACKAGED(2, "PACKAGE"),
    ORDER_EXPRESS(3, "EXPRESS"),
    ORDER_SUCCESS(4, "DEALT"),
    ORDER_CLOSED_BY_MALLUSER(-1, "CLOSED_BY_MALLUSER"),
    ORDER_CLOSED_BY_EXPIRED(-2, "CLOSED_BY_EXPIRED"),
    ORDER_CLOSED_BY_JUDGE(-3, "CLOSED_BY_JUDGE");

    private int orderStatus;

    private String name;

    public static NewBeeMallOrderStatusEnum getNewBeeMallOrderStatusEnumByStatus(int orderStatus) {
        return Arrays.stream(NewBeeMallOrderStatusEnum.values()).filter(status -> status.getOrderStatus() == orderStatus).findFirst().orElse(DEFAULT);
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
