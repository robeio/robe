package io.robe.convert.excel.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParseDate implements IsParser<Date> {

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
    public Date parse(Object o, Field field) {
        if (!isValid(o)) {
            return null;
        }
        JsonFormat formatAnn = field.getAnnotation(JsonFormat.class);
        if (formatAnn == null) {
            throw new RuntimeException("JsonFormat with pattern needed for: " + field.getName());
        }
        try {
            return new SimpleDateFormat(formatAnn.pattern(), Locale.getDefault()).parse(o.toString());
        } catch (ParseException e) {
            throw new RuntimeException("JsonFormat with pattern is wrong for: " + field.getName() + " pattern: " + formatAnn.pattern());
        }
    }

    @Override
    public void setCell(Date o, Cell cell, Field field) {
        if (o != null) {
            String format = field.getAnnotation(JsonFormat.class).pattern();
            cell.setCellValue(new SimpleDateFormat(format).format(o));
        }
    }

}
