package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseString implements IsParser<String> {
    @Override
    public String parse(Object o, Field field) {
        return isValid(o) ? String.valueOf(o.toString()) : null;
    }

    @Override
    public void setCell(String o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o.toString());
        }
    }
}
