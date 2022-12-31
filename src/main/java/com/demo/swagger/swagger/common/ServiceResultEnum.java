package com.demo.swagger.swagger.common;

public enum ServiceResultEnum {
    ERROR("error"),

    SUCCESS("success"),

    DATA_NOT_EXIST("data does not exist"),

    PARAM_ERROR("no record"),

    SAME_CATEGORY_EXIST("same category exist"),

    SAME_LOGIN_NAME_EXIST("same login name exist"),

    LOGIN_NAME_NULL("login name is required"),

    LOGIN_NAME_IS_NOT_PHONE("login name should be phone"),

    LOGIN_PASSWORD_NULL("password is required"),

    LOGIN_VERIFY_CODE_NULL("verify code is required"),

    LOGIN_VERIFY_CODE_ERROR("verify code error"),

    GOODS_NOT_EXIST("goods does not exist"),

    GOODS_PUT_DOWN("goods have been put down"),

    SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("item number limit"),

    SHOPPING_CART_ITEM_NUMBER_ERROR("item number must be great than 1"),

    SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR("item in cart limit"),

    SHOPPING_CART_ITEM_EXIST_ERROR("item exists"),

    LOGIN_ERROR("login fail"),

    NOT_LOGIN_ERROR("not login"),

    TOKEN_EXPIRE_ERROR("expire token"),

    USER_NULL_ERROR("user does not exist"),

    LOGIN_USER_LOCKED_ERROR("login user locked"),

    ORDER_NOT_EXIST_ERROR("order does not exist"),

    NULL_ADDRESS_ERROR("address should not be null"),

    ORDER_PRICE_ERROR("order price error"),

    ORDER_ITEM_NULL_ERROR("order item null"),

    ORDER_GENERATE_ERROR("generate order error"),

    SHOPPING_ITEM_ERROR("cart items error"),

    SHOPPING_ITEM_COUNT_ERROR("insufficient items"),

    ORDER_STATUS_ERROR("order status error"),

    OPERATE_ERROR("operate fail"),

    REQUEST_FORBIDEN_ERROR("operate forbiden"),

    DB_ERROR("database error");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
