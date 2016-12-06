package io.robe.common.utils.reflection;

import io.robe.common.utils.Arrays;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by kamilbukum on 17/11/16.
 */
public class Generics {

    public static ParameterizedType getGenericClass(Class<?> clazz) {
        Type superClass = clazz.getGenericSuperclass();
        if(superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        return (ParameterizedType) superClass;
    }


    public static ParameterizedType[] getTypes(Class<?> clazz){
        ParameterizedType parameterizedType = getGenericClass(clazz);
        return Arrays.cast(parameterizedType.getActualTypeArguments(), ParameterizedType.class);
    }

    public static ParameterizedType getType(Class<?> clazz, int index){
        ParameterizedType parameterizedType = getGenericClass(clazz);
        return (ParameterizedType) parameterizedType.getActualTypeArguments()[index];
    }

    public static <T> Class<T> getTypeClass(Class<?> clazz, int index){
        return (Class<T>) getType(clazz, index).getRawType();
    }

    public static Class<?> [] getTypeClasses(Class<?> clazz){
        ParameterizedType [] types = getTypes(clazz);
        Class<?> [] classes = new Class[types.length];

        for(int i = 0 ; i < types.length; i++ ) {
            classes[i] = (Class<?>) types[i].getRawType();
        }
        return classes;
    }
}
