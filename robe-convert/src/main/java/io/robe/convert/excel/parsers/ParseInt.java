package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ParseInt implements IsParser<Integer> {
    @Override
    public Integer parse(Object o, Field field) {
        if (isValid(o)) {
            Integer i;
            int ind = o.toString().indexOf(".");
            if (ind != -1) {
                o = o.toString().substring(0, ind);
            }
            i = Integer.valueOf(o.toString());
            return i;
        }
        return null;
    }

    @Override
    public void setCell(Integer o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o);
        }
    }
}
