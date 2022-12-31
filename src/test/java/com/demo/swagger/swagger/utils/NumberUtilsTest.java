package com.demo.swagger.swagger.utils;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class NumberUtilsTest {

    @Test
    void generateRandomNumber() {
        int result = NumberUtils.generateRandomNumber(4);
        System.out.println(result);
        assertEquals(4, String.valueOf(result).length());
    }
}