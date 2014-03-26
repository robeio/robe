package io.robe.convert.importer.excel.parsers;

import java.lang.reflect.Field;

public class StringParser implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        String s = String.valueOf(o.toString());

        return s;
    }
}
