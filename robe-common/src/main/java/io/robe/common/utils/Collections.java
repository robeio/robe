package io.robe.common.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kamilbukum on 11/11/16.
 */
public class Collections {
    public static <T> Set<T> cloneSetAndAdd(Set<T> set, T element) {
        Set<T> classSetNew = new HashSet<>();
        classSetNew.addAll(set);
        classSetNew.add(element);
        return classSetNew;
    }
}
