package io.robe.convert.xml.parsers;

public enum Parsers {
    BIGDECIMAL("java.math.BigDecimal", ParseBigDecimal.class),
    BOOLEAN("java.lang.Boolean", ParseBool.class),
    BYTE("java.lang.Byte", ParseChar.class),
    DOUBLE("java.lang.Double", ParseDouble.class),
    INT("int", ParseInt.class),
    INTEGER("java.lang.Integer", ParseInt.class),
    LONG("java.lang.Long", ParseLong.class),
    STRING("java.lang.String", ParseString.class),
    CHAR("java.lang.String", ParseChar.class),
    DATE("java.util.Date", ParseDate.class),
    ENUM("java.util.Enum", ParseEnum.class);


    private final String type;
    private final Class<? extends IsParser> parser;

    public IsParser getParser() {
        try {
            return (parser == null) ? null : parser.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }    }

    private Parsers(String type, Class<? extends IsParser> parser) {
        this.type = type;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return type;
    }
}
