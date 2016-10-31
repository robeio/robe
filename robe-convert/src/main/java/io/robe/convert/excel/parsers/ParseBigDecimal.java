package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class ParseBigDecimal implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        if (o == null)
            return null;
        return BigDecimal.valueOf(Double.valueOf(o.toString()));
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        BigDecimal bigDecimal = (BigDecimal) o;
        if (bigDecimal != null) {
            cell.setCellValue(bigDecimal.doubleValue());
        }
    }
}
