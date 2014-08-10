package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

/**
 * Created by hasanmumin on 08/08/14.
 */
public class ParseEnum implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        if (o != null) {
            return o.toString();
        }
        return "";
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        Enum anEnum = (Enum) o;
        if (anEnum != null) {
            cell.setCellValue(anEnum.name());
        }
    }
}
