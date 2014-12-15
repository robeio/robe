package io.robe.convert.csv.parsers;

import org.supercsv.cellprocessor.*;

public enum Parsers {
    BIGDECIMAL("java.math.BigDecimal", ParseBigDecimal.class),
    BOOLEAN("java.lang.Boolean", ParseBool.class),
    BYTE("java.lang.Byte", ParseChar.class),
    DOUBLE("java.lang.Double", ParseDouble.class),
    INT("int", ParseInt.class),
    INTEGER("java.lang.Integer", ParseInt.class),
    LONG("java.lang.Long", ParseLong.class),
    STRING("java.lang.String", null),
    CHAR("java.lang.String", ParseChar.class),
    ENUM("java.lang.Enum", ParseEnum.class);


    private final String type;
    private final Class<? extends CellProcessorAdaptor> parser;

    public CellProcessorAdaptor getParser() {
        try {
            return (parser == null) ? null : parser.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CellProcessorAdaptor getParser(Object... params) {
        try {
            CellProcessorAdaptor instance = parser.newInstance();
            if (instance instanceof ParseEnum)
                ((ParseEnum) instance).setEnumType((Class<? extends Enum>) params[0]);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Parsers(String type, Class<? extends CellProcessorAdaptor> parser) {
        this.type = type;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return type;
    }
}
