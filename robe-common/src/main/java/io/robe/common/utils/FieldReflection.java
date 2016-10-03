package io.robe.common.utils;

import com.google.common.collect.ImmutableSet;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class which helps field operations by reflectasm library.
 * It also has cache support for class fields.
 */
public class FieldReflection {

    private static final ConcurrentHashMap<Class<?>, ImmutableSet<Field>> cache = new ConcurrentHashMap<>();

    private FieldReflection() {

    }

    private static final ImmutableSet<Field> get(Class<?> clazz) {
        if (!cache.containsKey(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            cache.put(clazz, ImmutableSet.copyOf(fields));
            return cache.get(clazz);
        }
        return cache.get(clazz);
    }

    public static final <T> void copy(T src, T dest) {
        ImmutableSet<Field> fa = get(src.getClass());
        for (Field field : fa) {
            try {
                field.set(dest, field.get(src));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static final <T> void mergeRight(T src, T dest) {
        ImmutableSet<Field> fa = get(src.getClass());
        for (Field field : fa) {
            try {
                Object value = field.get(src);
                if (value == null)
                    continue;
                field.set(dest, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}