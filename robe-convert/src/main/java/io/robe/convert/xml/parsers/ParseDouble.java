package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParseDouble implements IsParser {
    @Override
    public Object parse(JsonParser parser, Field field) throws IOException {
        boolean isValid = parser.getValueAsString() != null && !parser.getValueAsString().isEmpty();
        return isValid ? new Double(parser.getValueAsDouble()) : null;
    }
}
