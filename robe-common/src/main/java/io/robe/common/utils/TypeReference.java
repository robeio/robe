package io.robe.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> {
    protected final Class<T> clazz;

    protected TypeReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if(superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        clazz = (Class<T>) ((ParameterizedType) ((ParameterizedType)superClass).getActualTypeArguments()[0]).getRawType();
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
