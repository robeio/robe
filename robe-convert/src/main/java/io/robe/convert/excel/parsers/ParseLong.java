package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseLong implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        Long l = null;

        int ind = ((String) o).indexOf(".");

        if (ind != -1) {
            o = ((String) o).substring(0, ind);
        }

        l = Long.valueOf(o.toString());

        return l;
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        Long aLong = (Long) o;
        cell.setCellValue(aLong);
    }
}
