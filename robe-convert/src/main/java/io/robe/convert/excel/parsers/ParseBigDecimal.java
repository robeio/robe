package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class ParseBigDecimal implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        BigDecimal bd = BigDecimal.valueOf(Double.valueOf(o.toString()));
        return bd;
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        BigDecimal bigDecimal = (BigDecimal) o;
        if (bigDecimal != null) {
            cell.setCellValue(bigDecimal.doubleValue());
        }
    }
}
