package io.robe.convert.excel;

import io.robe.convert.excel.parsers.*;

public enum Parsers {
    BIGDECIMAL("java.math.BigDecimal", new BigDecimalParser()),
    BOOLEAN("java.lang.Boolean",new BoolParser()),
    BYTE("java.lang.Byte",new ByteParser()),
    DOUBLE("java.lang.Double",new DoubleParser()),
    INT("int",new IntParser()),
    INTEGER("java.lang.Integer",new IntParser()),
    LONG("java.lang.Long", new LongParser()),
    STRING("java.lang.String",new StringParser()),
    DATE("java.util.Date",new DateParser());


    private final String type;
    private final IsParser parser;

    public IsParser getParser() {
        return parser;
    }

    private Parsers(String type,IsParser parser){
        this.type = type;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return  type;
    }
}
