package com.zhangyun.file.common.uilt;

import java.util.function.Function;

public class ObjectUtil {

    public static  <T, R> R map(T object, Function<T, R> function) {
        if (object == null || function == null) {
            return null;
        }
        return function.apply(object);
    }

    public static  <T, R> R mapOrDefault(T object, Function<T, R> function, R defaultValue) {
        R value = map(object, function);
        return value != null ? value : defaultValue;
    }

}
