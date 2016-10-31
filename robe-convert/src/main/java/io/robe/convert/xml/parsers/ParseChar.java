package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParseChar implements IsParser<Character> {
    @Override
    public Character parse(JsonParser parser, Field field) throws IOException {

        boolean isValid = parser.getValueAsString() != null && !parser.getValueAsString().trim().isEmpty();

        return isValid ? parser.getValueAsString().charAt(0) : null;
    }
}
