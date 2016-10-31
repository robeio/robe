package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParseChar implements IsParser<Character> {
    @Override
    public Character parse(JsonParser parser, Field field) throws IOException {
        return isValid(parser) ? parser.getValueAsString().charAt(0) : null;
    }
}
