package io.robe.convert.excel.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        String format = field.getAnnotation(JsonFormat.class).pattern();
        try {
            return new SimpleDateFormat(format).parse(o.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        Date date = (Date) o;
        cell.setCellValue(date);
        String format = field.getAnnotation(JsonFormat.class).pattern();
        cell.setCellValue(new SimpleDateFormat(format).format(date));
    }
}
