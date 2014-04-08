package io.robe.convert.excel.parsers;

public enum Parsers {
    BIGDECIMAL("java.math.BigDecimal", new ParseBigDecimal()),
    BOOLEAN("java.lang.Boolean", new ParseBool()),
    BYTE("java.lang.Byte", new ParseByte()),
    DOUBLE("java.lang.Double", new ParseDouble()),
    INT("int", new ParseInt()),
    INTEGER("java.lang.Integer", new ParseInt()),
    LONG("java.lang.Long", new ParseLong()),
    STRING("java.lang.String", new ParseString()),
    DATE("java.util.Date", new ParseDate());


    private final String type;
    private final IsParser parser;

    public IsParser getParser() {
        return parser;
    }

    private Parsers(String type, IsParser parser) {
        this.type = type;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return type;
    }
}
