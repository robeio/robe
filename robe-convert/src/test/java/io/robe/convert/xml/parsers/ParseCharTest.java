package io.robe.convert.xml.parsers;

import io.robe.convert.xml.ParserUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ParseCharTest {

    @Test
    public void parse() throws Exception {
        char expected = 'e';
        ParseChar parseChar = new ParseChar();
        char actual = parseChar.parse(ParserUtil.getParser("<char>e</char>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseTurkish() throws Exception {
        char expected = 'Ğ';
        ParseChar parseChar = new ParseChar();
        char actual = parseChar.parse(ParserUtil.getParser("<char>Ğ</char>"), null);
        assertEquals(expected, actual);

        actual = parseChar.parse(ParserUtil.getParser("<char>ü</char>"), null);
        assertEquals('ü', actual);
        actual = parseChar.parse(ParserUtil.getParser("<char>ı</char>"), null);
        assertEquals('ı', actual);
        actual = parseChar.parse(ParserUtil.getParser("<char>İ</char>"), null);
        assertEquals('İ', actual);
        actual = parseChar.parse(ParserUtil.getParser("<char>ö</char>"), null);
        assertEquals('ö', actual);
        actual = parseChar.parse(ParserUtil.getParser("<char>ş</char>"), null);
        assertEquals('ş', actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        Character expected = null;
        ParseChar parseChar = new ParseChar();
        Character actual = parseChar.parse(ParserUtil.getParser("<char></char>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseSpace() throws Exception {
        Character expected = null;
        ParseChar parseChar = new ParseChar();
        Character actual = parseChar.parse(ParserUtil.getParser("<char>   </char>"), null);
        assertEquals(expected, actual);
    }

}