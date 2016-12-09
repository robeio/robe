package io.robe.convert.xml.parsers;

import org.junit.Test;

import static io.robe.convert.xml.ParserUtil.getParser;
import static org.junit.Assert.*;

public class ParseStringTest {

    @Test
    public void parse() throws Exception {
        String expected = "42";
        ParseString parseString = new ParseString();
        String actual = parseString.parse(getParser("<string>42</string>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        String expected = null;
        ParseString parseString = new ParseString();
        String actual = parseString.parse(getParser("<string></string>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseSpace() throws Exception {
        String expected = null;
        ParseString parseString = new ParseString();
        String actual = parseString.parse(getParser("<string>   </string>"), null);
        assertEquals(expected, actual);
    }


}