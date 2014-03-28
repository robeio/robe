package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public interface IsParser {
    public Object parse(Object o,Field field);

    public void setCell(Object o, Cell cell, Field field);


}
