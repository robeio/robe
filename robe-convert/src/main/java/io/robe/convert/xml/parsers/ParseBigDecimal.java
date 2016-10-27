package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;

public class ParseBigDecimal implements IsParser {
    @Override
    public Object parse(JsonParser parser, Field field) throws IOException {
        try {
            return new BigDecimal(parser.getValueAsString());
        }catch (NullPointerException e){
            return  null;
        }
    }
}
