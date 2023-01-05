package com.demo.swagger.swagger.common;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum PayTypeEnum {

    DEFAULT(-1, "ERROR"),
    NOT_PAY(0, "NULL"),
    ALI_PAY(1, "ALIPAY"),
    WEIXIN_PAY(2, "WECHATPAY");

    private int payType;

    private String name;

    public static PayTypeEnum getPayTypeEnumByType(int payType) {
        return Arrays.stream(PayTypeEnum.values()).filter(type -> type.getPayType() == payType).findFirst().orElse(DEFAULT);
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
