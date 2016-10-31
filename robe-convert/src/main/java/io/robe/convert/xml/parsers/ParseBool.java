package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParseBool implements IsParser<Boolean>{
    @Override
    public Boolean parse(JsonParser parser, Field field) throws IOException {
        boolean isValid = parser.getValueAsString() != null && !parser.getValueAsString().trim().isEmpty();
        return isValid ? parser.getValueAsBoolean() : null;
    }
}
