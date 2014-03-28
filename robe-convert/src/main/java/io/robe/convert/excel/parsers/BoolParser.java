package io.robe.convert.excel.parsers;

import java.lang.reflect.Field;

public class BoolParser implements IsParser {
    @Override
    public Object parse(Object o,Field field) {
        Boolean b = null;

        if (o instanceof String) {
            b = Boolean.valueOf(o.toString());
        }

        return b;
    }
}
