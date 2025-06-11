package com.hochschild.speed.back.util;

public class AssertUtils {
    public static void isNotEmptyString(Object obj, String message) {
        if (StringUtils.isEmpty(obj)) {
            throw new IllegalArgumentException(message);
        }
    }
}
