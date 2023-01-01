package com.demo.swagger.swagger.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class BeanUtils {

    public static Object copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source == null) {
            return target;
        }

        org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    public static <T> List<T> copyList(List source, Class<T> clazz) {
        return copyList(source, clazz, null);
    }

    private static <T> List<T> copyList(List sources, Class<T> clazz, CallBack<T> callback) {
        List<T> targetList = new ArrayList<>();

        if (sources != null) {
            try {
                for (Object source: sources) {
                    T target = clazz.newInstance();
                    copyProperties(source, target);
                    if (callback != null) {
                        callback.set(source, target);
                    }

                    targetList.add(target);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return targetList;
    }

    public interface CallBack<T> {
        void set(Object source, T target);
    }
}
