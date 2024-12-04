package com.vitaldev.vitallibs.util;

public class StringUtil {
    public static String padLeft(String text, int length) {
        return String.format("%" + length + "s", text);
    }

    public static String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }

    public static String join(String[] array, String separator) {
        return String.join(separator, array);
    }

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    public static String safeSubstring(String text, int start, int end) {
        if (text == null || text.length() < start) return "";
        end = Math.min(end, text.length());
        return text.substring(start, end);
    }

    public static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static String trimWhitespace(String text) {
        if (text == null) return null;
        return text.trim();
    }

    public static String reverse(String text) {
        if (text == null) return null;
        return new StringBuilder(text).reverse().toString();
    }

    public static String toUpperCaseFirstChar(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String toLowerCaseFirstChar(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }

    public static String repeat(String text, int count) {
        if (text == null || count <= 0) return "";
        StringBuilder result = new StringBuilder(text.length() * count);
        result.append(text.repeat(count));
        return result.toString();
    }
}