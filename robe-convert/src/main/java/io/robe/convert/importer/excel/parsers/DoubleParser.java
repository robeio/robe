package io.robe.convert.importer.excel.parsers;

import java.lang.reflect.Field;

public class DoubleParser implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        Double d = Double.valueOf(o.toString());

        return d;
    }
}
