package io.robe.common.utils;

public class Validations {

    private Validations() {

    }

    public static boolean isEmptyOrNull(String pattern) {
        return pattern == null || pattern.trim().equals("");
    }
}
