package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseDouble implements IsParser<Double> {
    @Override
    public Double parse(Object o, Field field) {
        if (o == null)
            return null;
        return Double.valueOf(o.toString());
    }

    @Override
    public void setCell(Double o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o);
        }
    }
}
