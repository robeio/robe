package io.robe.convert.importer.excel.parsers;

import java.lang.reflect.Field;

public class LongParser implements IsParser {
    @Override
    public Object parse(Object o, Field field) {
        Long l = null;

        int ind = ((String) o).indexOf(".");

        if (ind != -1) {
            o = ((String) o).substring(0, ind);
        }

        l = Long.valueOf(o.toString());

        return l;
    }
}
