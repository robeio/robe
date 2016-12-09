package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class ParseBigDecimal implements IsParser<BigDecimal> {
    @Override
    public BigDecimal parse(Object o, Field field) {
        return isValid(o) ? new BigDecimal(o.toString()) : null;
    }

    @Override
    public void setCell(BigDecimal o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o.doubleValue());
        }
    }
}
