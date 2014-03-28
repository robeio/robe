package io.robe.convert.excel.parsers;

import java.lang.reflect.Field;

public interface IsParser {
    public Object parse(Object o,Field field);


}
