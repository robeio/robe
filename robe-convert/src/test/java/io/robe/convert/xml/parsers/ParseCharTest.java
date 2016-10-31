package io.robe.convert.xml.parsers;

import io.robe.convert.xml.ParserUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ParseCharTest {
    @Test
    public void parse() throws Exception {
        char expected = 'e';
        ParseChar parseChar = new ParseChar();
        char actual = (char) parseChar.parse(ParserUtil.getParser("<char>e</char>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        Object expected = null;
        ParseChar parseChar = new ParseChar();
        Object actual = parseChar.parse(ParserUtil.getParser("<char></char>"), null);
        assertEquals(expected, actual);
    }

}