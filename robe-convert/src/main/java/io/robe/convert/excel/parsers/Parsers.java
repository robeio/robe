package io.robe.convert.excel.parsers;

public enum Parsers {

    BIGDECIMAL(ParseBigDecimal.class),
    BOOLEAN(ParseBool.class),
    BYTE(ParseChar.class),
    DOUBLE(ParseDouble.class),
    INT(ParseInt.class),
    INTEGER(ParseInt.class),
    LONG(ParseLong.class),
    STRING(ParseString.class),
    CHAR(ParseChar.class),
    CHARACTER(ParseChar.class),
    DATE(ParseDate.class),
    ENUM(ParseEnum.class);


    private final Class<? extends IsParser> parser;

    Parsers(Class<? extends IsParser> parser) {
        this.parser = parser;
    }

    public IsParser getParser() {
        try {
            return parser.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
