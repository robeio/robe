package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class IntParser implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        Integer i = null;

        int ind = ((String) o).indexOf(".");

        if (ind != -1) {
            o = ((String) o).substring(0, ind);
        }

        i = Integer.valueOf(o.toString());

        return i;
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        Integer integer = (Integer) o;
        cell.setCellValue(integer);
    }
}
