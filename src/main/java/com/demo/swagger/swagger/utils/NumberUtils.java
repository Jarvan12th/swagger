package com.demo.swagger.swagger.utils;

import java.util.regex.Pattern;

public class NumberUtils {
    public static int generateRandomNumber(int length) {
        // Math.random() returns a double value with a positive sign, range at [0, 1)
        // Math.random() * 9 + 1 returns a double value with a positive sign, range at [1, 10)
        // Math.pow(10, length - 1) returns a double value that equals to 10 ^ (length - 1)
        return (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
    }

    public static boolean isPhone(String loginName) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0-8])|(18[0-9]))\\d{8}$");
        return pattern.matcher(loginName).matches();
    }

    public static String generateOrderNo() {
        StringBuffer buffer = new StringBuffer(String.valueOf(System.currentTimeMillis()));
        buffer.append(generateRandomNumber(4));

        return buffer.toString();
    }
}
