package io.robe.common.utils.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kamilbukum on 30/01/2017.
 */
public class Generics {

    public static <T> Class<T> getTypeOfGenerics(Class<?> clazz){
        ParameterizedType type = (ParameterizedType)clazz.getGenericSuperclass();
        TypeVariable variable = (TypeVariable)type.getActualTypeArguments()[0];
        System.out.println(variable.getBounds()[0]);
        return null;
    }

    public static void main(String[] args) {
        List<String> list = new LinkedList<>();
        System.out.println(getTypeOfGenerics(list.getClass()));
    }
}
