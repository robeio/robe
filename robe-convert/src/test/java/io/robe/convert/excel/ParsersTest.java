package io.robe.convert.excel;

import io.robe.convert.excel.parsers.Parsers;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Locale;

public class ParsersTest {
    @Test
    public void testSwitch() throws Exception {

        String clazz = BigDecimal.class.getSimpleName().toUpperCase(Locale.ENGLISH);
        System.out.println(Parsers.valueOf(clazz).getParser().getClass().getName());

        clazz = Boolean.class.getSimpleName().toUpperCase(Locale.ENGLISH);
        System.out.println(Parsers.valueOf(clazz).getParser().getClass().getName());

        clazz = Byte.class.getSimpleName().toUpperCase(Locale.ENGLISH);
        System.out.println(Parsers.valueOf(clazz).getParser().getClass().getName());

        clazz = Double.class.getSimpleName().toUpperCase(Locale.ENGLISH);
        System.out.println(Parsers.valueOf(clazz).getParser().getClass().getName());

        clazz = int.class.getSimpleName().toUpperCase(Locale.ENGLISH);
        System.out.println(Parsers.valueOf(clazz).getParser().getClass().getName());

        clazz = Integer.class.getSimpleName().toUpperCase(Locale.ENGLISH);
        System.out.println(Parsers.valueOf(clazz).getParser().getClass().getName());

        clazz = Long.class.getSimpleName().toUpperCase(Locale.ENGLISH);
        System.out.println(Parsers.valueOf(clazz).getParser().getClass().getName());

        clazz = String.class.getSimpleName().toUpperCase(Locale.ENGLISH);
        System.out.println(Parsers.valueOf(clazz.toUpperCase(Locale.ENGLISH)).getParser().getClass().getName());

    }
}
