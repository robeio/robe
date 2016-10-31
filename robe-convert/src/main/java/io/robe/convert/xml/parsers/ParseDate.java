package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ParseDate implements IsParser {
    @Override
    public Object parse(JsonParser parser, Field field) throws IOException {
        try {
            boolean isValid = parser.getValueAsString() != null && !parser.getValueAsString().trim().isEmpty();
            if (!isValid)
                return null;
            String format = field.getAnnotation(JsonFormat.class).pattern();
            return new SimpleDateFormat(format).parse(parser.getValueAsString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
