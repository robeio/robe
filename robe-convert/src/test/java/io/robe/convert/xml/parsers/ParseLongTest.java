package io.robe.convert.xml.parsers;

import org.junit.Test;

import static io.robe.convert.xml.ParserUtil.getParser;
import static org.junit.Assert.*;

public class ParseLongTest {
    @Test
    public void parse() throws Exception {
        Long expected = 42l;
        ParseLong parseInt = new ParseLong();
        Long actual = (Long) parseInt.parse(getParser("<long>42</long>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        Long expected = null;
        ParseLong parseInt = new ParseLong();
        Long actual = (Long) parseInt.parse(getParser("<long></long>"), null);
        assertEquals(expected, actual);
    }

}