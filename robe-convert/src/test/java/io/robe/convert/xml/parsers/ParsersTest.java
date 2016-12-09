package io.robe.convert.xml.parsers;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ParsersTest {
    private  static HashMap<String, Class<? extends IsParser>> parserMap = new HashMap<>();

    @BeforeClass
    public static void createHashmap() {
        parserMap.put("BIGDECIMAL", ParseBigDecimal.class);
        parserMap.put("BOOLEAN", ParseBool.class);
        parserMap.put("BYTE", ParseChar.class);
        parserMap.put("DOUBLE", ParseDouble.class);
        parserMap.put("INT", ParseInt.class);
        parserMap.put("INTEGER", ParseInt.class);
        parserMap.put("LONG", ParseLong.class);
        parserMap.put("STRING", ParseString.class);
        parserMap.put("CHAR", ParseChar.class);
        parserMap.put("CHARACTER", ParseChar.class);
        parserMap.put("DATE", ParseDate.class);
        parserMap.put("ENUM", ParseEnum.class);
    }

    @Test
    public void getParser() throws Exception {
        for (Map.Entry<String, Class<? extends IsParser>> entry : parserMap.entrySet()) {
            assert(Parsers.valueOf(entry.getKey()).getParser().getClass().equals(entry.getValue()));
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void getParserNull() throws Exception {
        assertTrue(Parsers.valueOf("invalidParser").getParser() == null);
    }

}