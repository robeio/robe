package io.robe.convert.excel.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class ParseDate implements IsParser {
    // Default values with 
    private static final String DEFAULT_DATE_FORMAT1 = "dd.MM.yyyy";
    private static final String DEFAULT_DATE_FORMAT2 = "dd.MM.yy";
    private static final String DEFAULT_DATE_FORMAT3 = "yyyy/MM/dd";
    private static final String DEFAULT_DATE_FORMAT4 = "yy/MM/dd";
    private static final String DEFAULT_DATE_FORMAT5 = "dd/MM/yy";
    private static final String DEFAULT_DATE_FORMAT6 = "dd/MM/yyyy";
    private static final String DEFAULT_DATE_FORMAT7 = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_FORMAT8 = "yy-MM-dd";
    private static final String DEFAULT_DATE_FORMAT9 = "ddMMyy";
    private static final String DEFAULT_DATE_FORMAT10 = "ddMMyyyy";
    private static final String DEFAULT_DATE_FORMAT11 = "dd-MMM-yyyy";
    private static final String DEFAULT_DATE_FOMRAT12 = "ddd MMM dd hh:mm:ss zzzz yyyy";
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseDate.class);


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
        Date date = null;
        String columnValue = o.toString();
        JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
        if (columnValue != null && columnValue.length() > 1) {
            if (jsonFormat != null) {
                try {
                    date = formatWithGivenPattern(columnValue, jsonFormat.pattern());
                } catch (ParseException e) {
                    date = formatWithDefaults(columnValue);
                }
            } else {
                date = formatWithDefaults(columnValue);
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

    /**
     * Uses Java's @DateUtil.class for parsing operation,
     * It may not be efficient , because it tries to parse with
     * all known patterns
     *
     * @param columnValue Date column value
     * @return Valid date object
     */
    private Date formatWithDefaults(String columnValue) {
        Date formattedDate;
        try {
            LinkedList<String> dateFormats = new LinkedList<String>();
            dateFormats.add(DEFAULT_DATE_FORMAT1);
            dateFormats.add(DEFAULT_DATE_FORMAT2);
            dateFormats.add(DEFAULT_DATE_FORMAT3);
            dateFormats.add(DEFAULT_DATE_FORMAT4);
            dateFormats.add(DEFAULT_DATE_FORMAT5);
            dateFormats.add(DEFAULT_DATE_FORMAT6);
            dateFormats.add(DEFAULT_DATE_FORMAT7);
            dateFormats.add(DEFAULT_DATE_FORMAT8);
            dateFormats.add(DEFAULT_DATE_FORMAT9);
            dateFormats.add(DEFAULT_DATE_FORMAT10);
            dateFormats.add(DEFAULT_DATE_FORMAT11);
            dateFormats.add(DEFAULT_DATE_FOMRAT12);
            formattedDate = new Date();
        } catch (Exception e) {
            LOGGER.error("Couldn't determine the date format of the field" + columnValue + "   error message : " + e.getMessage());
            throw new RuntimeException("Unknown date format " + columnValue, e);
        }
        return formattedDate;
    }
}
