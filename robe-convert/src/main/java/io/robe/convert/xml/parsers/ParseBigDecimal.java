package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;

public class ParseBigDecimal implements IsParser<BigDecimal> {
    @Override
    public BigDecimal parse(JsonParser parser, Field field) throws IOException {
        boolean isValid = parser.getValueAsString() != null && !parser.getValueAsString().trim().isEmpty();
        return isValid ? new BigDecimal(parser.getValueAsString()) : null;

    }
}
