package io.robe.common.utils;

/**
 * Created by hasanmumin on 05/11/14.
 */
public class Validations {


    public static boolean isEmptyOrNull(String pattern) {
        return pattern == null || pattern.trim().equals("");
    }
}
