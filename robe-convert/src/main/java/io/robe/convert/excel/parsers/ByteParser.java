package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ByteParser implements IsParser {
    @Override
    public Object parse(Object o,Field field) {
        Byte b = null;

        if (o instanceof String) {
            b = Byte.valueOf(o.toString());
        }

        return b;
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        Byte aByte = (Byte)o;
        cell.setCellErrorValue(aByte);
    }
}
