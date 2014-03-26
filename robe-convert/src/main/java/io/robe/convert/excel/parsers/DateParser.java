package io.robe.convert.excel.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by kaanalkim on 26/03/14.
 */
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
}
