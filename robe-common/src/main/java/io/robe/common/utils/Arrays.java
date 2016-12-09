package io.robe.common.utils;

import java.lang.reflect.Array;

/**
 * Created by kamilbukum on 11/11/16.
 */
public class Arrays {

    /**
     *
     * @param array
     * @param element
     * @param <T>
     * @return
     */
    public static <T> boolean isExist(Object[] array, T element) {
        if(array == null) return false;

        for(Object inElement: array) {
            if(inElement != null && inElement.equals(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param fromArray
     * @param clazz
     * @param <F>
     * @param <T>
     * @return
     */
    public static <F, T> T[] cast(F[] fromArray, Class<T> clazz) {
        if(fromArray == null) return null;

        final T[] toArray = (T[]) Array.newInstance(clazz, fromArray.length);
        if(fromArray.length == 0) return toArray;

        for(int i = 0 ; i< fromArray.length; i++) {
            toArray[i] = (T)fromArray[i];
        }
        return toArray;
    }

}
