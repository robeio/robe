package io.robe.convert.xml.parsers;

import org.junit.Test;

import java.lang.reflect.Field;

import static io.robe.convert.xml.ParserUtil.getParser;
import static org.junit.Assert.*;

public class ParseEnumTest {
    public TestEnum testEnum;

    @Test
    public void parse() throws Exception {
        TestEnum expected = TestEnum.ON;
        Field field = getClass().getField("testEnum");
        ParseEnum parseEnum = new ParseEnum();
        TestEnum actual = (TestEnum) parseEnum.parse(getParser("<enum>ON</enum>"), field);
        assertEquals(expected, actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        TestEnum expected = null;
        Field field = getClass().getField("testEnum");
        ParseEnum parseEnum = new ParseEnum();
        TestEnum actual = (TestEnum) parseEnum.parse(getParser("<enum></enum>"), field);
        assertEquals(expected, actual);
    }

    private enum TestEnum {
        ON,
        OFF
    }

}