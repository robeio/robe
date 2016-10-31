package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParseDate implements IsParser<Date> {
    @Override
    public Date parse(JsonParser parser, Field field) throws IOException {
        try {
            if (!isValid(parser))
                return null;
            String format = field.getAnnotation(JsonFormat.class).pattern();
            return new SimpleDateFormat(format, Locale.getDefault()).parse(parser.getValueAsString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
