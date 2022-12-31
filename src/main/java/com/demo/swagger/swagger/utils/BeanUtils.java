package com.demo.swagger.swagger.utils;

public abstract class BeanUtils {

    public static Object copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source == null) {
            return target;
        }

        org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }
}
