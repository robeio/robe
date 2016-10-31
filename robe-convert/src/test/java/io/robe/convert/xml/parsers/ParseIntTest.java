package io.robe.convert.xml.parsers;

import org.junit.Test;

import static io.robe.convert.xml.ParserUtil.getParser;
import static org.junit.Assert.*;

public class ParseIntTest {
    @Test
    public void parse() throws Exception {
        Integer expected = 42;
        ParseInt parseInt = new ParseInt();
        Integer actual = (Integer) parseInt.parse(getParser("<int>42</int>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        Integer expected = null;
        ParseInt parseInt = new ParseInt();
        Integer actual = (Integer) parseInt.parse(getParser("<int></int>"), null);
        assertEquals(expected, actual);
    }
}