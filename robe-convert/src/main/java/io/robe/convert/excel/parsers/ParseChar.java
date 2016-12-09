package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseChar implements IsParser<Character> {
    @Override
    public Character parse(Object o, Field field) {
        return isValid(o) ? o.toString().charAt(0) : null;
    }

    @Override
    public void setCell(Character o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o.toString());
        }
    }
}
