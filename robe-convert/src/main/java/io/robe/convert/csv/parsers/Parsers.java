package io.robe.convert.csv.parsers;

import org.supercsv.cellprocessor.*;

public enum Parsers {
    BIGDECIMAL(ParseBigDecimal.class),
    BOOLEAN(ParseBool.class),
    BYTE(ParseChar.class),
    DOUBLE(ParseDouble.class),
    INT(ParseInt.class),
    INTEGER(ParseInt.class),
    LONG(ParseLong.class),
    STRING(null),
    CHAR(ParseChar.class),
    CHARACTER(ParseChar.class),
    ENUM(ParseEnum.class);

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

    Parsers(Class<? extends CellProcessorAdaptor> parser) {
        this.parser = parser;
    }

}
