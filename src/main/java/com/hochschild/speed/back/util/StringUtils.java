package com.hochschild.speed.back.util;

public class StringUtils {
    public static boolean isEmpty(Object obj) {
        return (obj == null) || (((String) obj).trim().isEmpty());
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static String getFullPathWithoutSeparatorAtTheEnd(String... paths) {
        StringBuilder result = new StringBuilder();

        for (String path : paths) {
            result.append("/").append(path);
        }

        result = new StringBuilder(result.toString().replaceAll("[/]+", "/"));
        return (result.toString().endsWith("/")) ? result.substring(0, result.lastIndexOf("/")) : result.toString();
    }
}
