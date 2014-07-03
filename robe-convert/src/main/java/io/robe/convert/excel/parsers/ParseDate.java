package io.robe.convert.excel.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yammer.dropwizard.validation.InvalidEntityException;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class ParseDate implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        String format = field.getAnnotation(JsonFormat.class).pattern();
        try {
            return formatDate(o.toString(), format);
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

    private Date formatDate(String columnValue, String dateFormat) throws ParseException {
        Date formattedDate = null;
        try {
            LinkedList<String> dateFormats = new LinkedList();
            dateFormats.add("yyyy/MM/dd");
            dateFormats.add("dd/MM/yy");
            dateFormats.add("dd/MM/yyyy");
            dateFormats.add("yyyy-MM-dd");
            dateFormats.add("yy-MM-dd");
            dateFormats.add("ddMMyy");
            dateFormats.add("ddMMyyyy");
            dateFormats.add("dd-MMM-yyyy");
            Date date = DateUtil.parseDate(columnValue, dateFormats);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            formattedDate = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (DateParseException e) {
            throw new InvalidEntityException("Unknown date format", Arrays.asList("Unknown date format"));
        }
        return formattedDate;
    }
}
