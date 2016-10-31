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
        if (!isValid(parser)) {
            return null;
        }
        JsonFormat formatAnn = field.getAnnotation(JsonFormat.class);
        if (formatAnn == null) {
            throw new RuntimeException("JsonFormat with pattern needed for: " + field.getName());
        }
        try {
            return new SimpleDateFormat(formatAnn.pattern(), Locale.getDefault()).parse(parser.getValueAsString());
        } catch (ParseException e) {
            throw new RuntimeException("JsonFormat with pattern is wrong for: " + field.getName() + " pattern: " + formatAnn.pattern());
        }
    }
}
