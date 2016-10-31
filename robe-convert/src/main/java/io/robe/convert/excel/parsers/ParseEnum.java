package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

import static java.lang.Enum.valueOf;

public class ParseEnum implements IsParser<Enum> {

    @Override
    public Enum parse(Object o, Field field) {
        if (!field.getType().isEnum())
            return null;
        Class<? extends Enum> type = (Class<? extends Enum>) field.getType();
        Enum anEnum = valueOf(type, o.toString());
        return anEnum;
    }

    @Override
    public void setCell(Enum o, Cell cell, Field field) {
        if (o != null) {
            cell.setCellValue(o.name());
        }
    }

}
