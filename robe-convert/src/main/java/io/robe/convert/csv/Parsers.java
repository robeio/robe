package io.robe.convert.csv;

import org.supercsv.cellprocessor.*;

public enum Parsers {
    BIGDECIMAL("java.math.BigDecimal", new io.robe.convert.csv.supercsv.ParseBigDecimal()),
    BOOLEAN("java.lang.Boolean",new ParseBool()),
    BYTE("java.lang.Byte",new ParseChar()),
    DOUBLE("java.lang.Double",new ParseDouble()),
    INT("int",new ParseInt()),
    INTEGER("java.lang.Integer",new ParseInt()),
    LONG("java.lang.Long", new ParseLong()),
    STRING("java.lang.String",null),
    CHAR("java.lang.String",new ParseChar());


    private final String type;
    private final CellProcessorAdaptor parser;

    public CellProcessorAdaptor getParser() {
        return parser;
    }

    private Parsers(String type, CellProcessorAdaptor parser){
        this.type = type;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return  type;
    }
}
