package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParseString implements IsParser<String> {
    @Override
    public String parse(JsonParser parser, Field field) throws IOException {
        return isValid(parser) ? parser.getValueAsString() : null;
    }
}
