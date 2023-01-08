package com.demo.swagger.swagger.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MD5UtilsTest {

    @Test
    public void MD5Encode() {
        assertEquals("e10adc3949ba59abbe56e057f20f883e", MD5Utils.MD5Encode("123456"));
    }
}