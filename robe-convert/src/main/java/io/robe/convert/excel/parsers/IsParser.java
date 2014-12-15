package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public interface IsParser<T> {
    public T parse(Object o, Field field);
    public void setCell(T o, Cell cell, Field field);
}
