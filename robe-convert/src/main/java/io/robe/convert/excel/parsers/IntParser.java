package io.robe.convert.excel.parsers;

import java.lang.reflect.Field;

public class IntParser implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        Integer i = null;

        int ind = ((String) o).indexOf(".");

        if (ind != -1) {
            o = ((String) o).substring(0, ind);
        }

        i = Integer.valueOf(o.toString());

        return i;
    }
}
