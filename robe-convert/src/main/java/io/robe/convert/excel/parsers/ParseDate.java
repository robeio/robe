package io.robe.convert.excel.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseDate implements IsParser {

    /**
     * First it checks is there any annotation class for parsing operations,
     * if it is, parses with given format, if there is a exception while
     * parsing with given format catches and tries with default values,
     * If there is no given format, tries with static values
     *
     * @param o     Object from cell value
     * @param field Field from given pojo
     * @return Valid date after parsing with pattern
     */
    @Override
    public Object parse(Object o, Field field) {

        if (o == null || o.toString().trim().length() == 0)
            return null;

        Date date = null;
        String columnValue = o.toString();

        if (columnValue.length() > 1) {
            JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
            if (jsonFormat != null) {
                try {
                    date = formatWithGivenPattern(columnValue, jsonFormat.pattern());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("JsonFormat with pattern needed for: " + field.getName());
            }
        }
        return date;
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {

        Date date = (Date) o;
        if (date != null) {
            cell.setCellValue(date);
            String format = field.getAnnotation(JsonFormat.class).pattern();
            cell.setCellValue(new SimpleDateFormat(format).format(date));
        }
    }

    /**
     * Tries to parse with annotated pattern
     *
     * @param columnValue Date column value
     * @param format      Given patter
     * @return Valid date object
     * @throws ParseException
     */
    private Date formatWithGivenPattern(String columnValue, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(columnValue);
    }
}
