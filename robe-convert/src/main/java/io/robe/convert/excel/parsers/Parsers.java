package io.robe.convert.excel.parsers;

public enum Parsers {
    BIGDECIMAL("java.math.BigDecimal", ParseBigDecimal.class),
    BOOLEAN("java.lang.Boolean", ParseBool.class),
    BYTE("java.lang.Byte", ParseByte.class),
    DOUBLE("java.lang.Double", ParseDouble.class),
    INT("int", ParseInt.class),
    INTEGER("java.lang.Integer", ParseInt.class),
    LONG("java.lang.Long", ParseLong.class),
    STRING("java.lang.String", ParseString.class),
    DATE("java.util.Date", ParseDate.class),
    ENUM("java.lang.String", ParseEnum.class);


    private final String type;
    private final Class<? extends IsParser> parser;

    Parsers(String type, Class<? extends IsParser> parser) {
        this.type = type;
        this.parser = parser;
    }

    public IsParser getParser() {
        try {
            return parser.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return type;
    }
}
