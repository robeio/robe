package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseDouble implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        Double d = Double.valueOf(o.toString());

        return d;
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        Double aDouble = (Double) o;
        cell.setCellValue(aDouble);
    }
}
