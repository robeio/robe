package io.robe.convert.importer.excel.parsers;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Created by kaanalkim on 26/03/14.
 */
public class BigDecimalParser implements IsParser {
    @Override
    public Object parse(Object o,Field field) {
        BigDecimal bd = BigDecimal.valueOf(Double.valueOf(o.toString()));

        return bd;
    }
}
