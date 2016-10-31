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
    public void parseTurkish() throws Exception {
        char expected = 'Ğ';
        ParseChar parseChar = new ParseChar();
        char actual = (char) parseChar.parse(ParserUtil.getParser("<char>Ğ</char>"), null);
        assertEquals(expected, actual);

        actual = (char) parseChar.parse(ParserUtil.getParser("<char>ü</char>"), null);
        assertEquals('ü', actual);
        actual = (char) parseChar.parse(ParserUtil.getParser("<char>ı</char>"), null);
        assertEquals('ı', actual);
        actual = (char) parseChar.parse(ParserUtil.getParser("<char>İ</char>"), null);
        assertEquals('İ', actual);
        actual = (char) parseChar.parse(ParserUtil.getParser("<char>ö</char>"), null);
        assertEquals('ö', actual);
        actual = (char) parseChar.parse(ParserUtil.getParser("<char>ş</char>"), null);
        assertEquals('ş', actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        Object expected = null;
        ParseChar parseChar = new ParseChar();
        Object actual = parseChar.parse(ParserUtil.getParser("<char></char>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseSpace() throws Exception {
        Object expected = null;
        ParseChar parseChar = new ParseChar();
        Object actual = parseChar.parse(ParserUtil.getParser("<char>   </char>"), null);
        assertEquals(expected, actual);
    }

}