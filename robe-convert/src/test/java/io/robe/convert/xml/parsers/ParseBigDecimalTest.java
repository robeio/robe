package io.robe.convert.xml.parsers;

import org.junit.Test;

import static io.robe.convert.xml.ParserUtil.getParser;
import static org.junit.Assert.*;


import java.math.BigDecimal;

public class ParseBigDecimalTest {
    @Test
    public void parse() throws Exception {
        BigDecimal expected = new BigDecimal("1000000000000000000000000.012");
        ParseBigDecimal parseBigDecimal = new ParseBigDecimal();
        BigDecimal actual = (BigDecimal) parseBigDecimal.parse(getParser("<bigdec>1000000000000000000000000.012</bigdec>"), null);
        assertEquals(expected, actual);
    }
    @Test
    public void parseEmpty() throws Exception {
        BigDecimal expected = null;
        ParseBigDecimal parseBigDecimal = new ParseBigDecimal();
        BigDecimal actual = (BigDecimal) parseBigDecimal.parse(getParser("<bigdec></bigdec>"), null);
        assertEquals(expected, actual);
    }

}