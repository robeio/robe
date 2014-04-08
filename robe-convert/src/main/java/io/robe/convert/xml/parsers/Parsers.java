package io.robe.convert.xml.parsers;

public enum Parsers {
    BIGDECIMAL("java.math.BigDecimal", new ParseBigDecimal()),
    BOOLEAN("java.lang.Boolean", new ParseBool()),
    BYTE("java.lang.Byte", new ParseChar()),
    DOUBLE("java.lang.Double", new ParseDouble()),
    INT("int", new ParseInt()),
    INTEGER("java.lang.Integer", new ParseInt()),
    LONG("java.lang.Long", new ParseLong()),
    STRING("java.lang.String", new ParseString()),
    CHAR("java.lang.String", new ParseChar()),
    DATE("java.util.Date", new ParseDate());


    private final String type;
    private final Parser parser;

    public Parser getParser() {
        return parser;
    }

    private Parsers(String type, Parser parser) {
        this.type = type;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return type;
    }
}
