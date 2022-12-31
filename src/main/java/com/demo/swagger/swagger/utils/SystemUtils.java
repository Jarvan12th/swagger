package com.demo.swagger.swagger.utils;

import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SystemUtils {
    public static String generateToken(String src) {
        if (StringUtils.isEmpty(src)) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result = new BigInteger(1, md.digest()).toString(16);
            if (result.length() == 31) {
                result = result + "-";
            }

            System.out.println(result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
