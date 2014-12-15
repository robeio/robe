package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

import static java.lang.Enum.valueOf;

public class ParseEnum implements IsParser {

    @Override
    public Object parse(Object o, Field field) {
        if (!field.getType().isEnum())
            return null;
        Class<? extends Enum> type = (Class<? extends Enum>) field.getType();
        Enum anEnum = valueOf(type, o.toString());
        return anEnum;
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        Enum anEnum = (Enum) o;
        if (anEnum != null) {
            cell.setCellValue(anEnum.name());
        }
    }

}
