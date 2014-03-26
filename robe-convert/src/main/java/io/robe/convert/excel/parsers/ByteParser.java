package io.robe.convert.excel.parsers;

import java.lang.reflect.Field;

public class ByteParser implements IsParser {
    @Override
    public Object parse(Object o,Field field) {
        Byte b = null;

        if (o instanceof String) {
            b = Byte.valueOf(o.toString());
        }

        return b;
    }
}
