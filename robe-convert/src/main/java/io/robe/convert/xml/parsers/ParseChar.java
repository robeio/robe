package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParseChar implements IsParser {
    @Override
    public Object parse(JsonParser parser, Field field) throws IOException {
        return parser.getValueAsString().charAt(0);
    }
}
