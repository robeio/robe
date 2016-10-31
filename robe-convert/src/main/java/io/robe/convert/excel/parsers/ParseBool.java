package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseBool implements IsParser<Boolean> {
    @Override
    public Boolean parse(Object o, Field field) {
        Boolean b = null;

        if (o instanceof String) {
            b = Boolean.valueOf(o.toString());
        }

        return b;
    }

    @Override
    public void setCell(Boolean o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o);
        }
    }
}
