package com.demo.swagger.swagger.utils;

public class NumberUtils {
    public static int generateRandomNumber(int length) {
        // Math.random() returns a double value with a positive sign, range at [0, 1)
        // Math.random() * 9 + 1 returns a double value with a positive sign, range at [1, 10)
        // Math.pow(10, length - 1) returns a double value that equals to 10 ^ (length - 1)
        return (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
    }
}
