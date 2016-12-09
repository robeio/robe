package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseLong implements IsParser<Long> {
    @Override
    public Long parse(Object o, Field field) {
        if (isValid(o)) {
            Long l;
            int ind = o.toString().indexOf(".");
            if (ind != -1) {
                o = o.toString().substring(0, ind);
            }
            l = Long.valueOf(o.toString());
            return l;
        }
        return null;
    }

    @Override
    public void setCell(Long o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o);
        }
    }
}
