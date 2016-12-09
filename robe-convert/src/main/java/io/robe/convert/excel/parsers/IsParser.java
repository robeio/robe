package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public interface IsParser<T> {
    T parse(Object o, Field field);

    void setCell(T o, Cell cell, Field field);

    default boolean isValid(Object o) {
        return o != null && !o.toString().trim().isEmpty();
    }

}
