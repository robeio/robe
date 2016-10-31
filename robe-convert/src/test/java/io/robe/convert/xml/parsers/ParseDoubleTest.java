package io.robe.convert.xml.parsers;

import org.junit.Test;

import static io.robe.convert.xml.ParserUtil.getParser;
import static org.junit.Assert.*;

public class ParseDoubleTest {
    @Test
    public void parse() throws Exception {
        Double expected = 12d;
        ParseDouble parseDouble = new ParseDouble();
        Double actual = (Double) parseDouble.parse(getParser("<double>12.00</double>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        Double expected = null;
        ParseDouble parseDouble = new ParseDouble();
        Double actual = (Double) parseDouble.parse(getParser("<double></double>"), null);
        assertEquals(expected, actual);
    }

}