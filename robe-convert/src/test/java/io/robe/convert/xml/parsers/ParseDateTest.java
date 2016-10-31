package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.robe.convert.xml.ParserUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class ParseDateTest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    public Date testDate;

    public Date testDateNoPattern;

    @Test
    public void parse() throws Exception {
        Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse("2016-10-31 09:10:12.2");
        Field field = getClass().getField("testDate");
        ParseDate parseDate = new ParseDate();
        Date actual = parseDate.parse(ParserUtil.getParser("<date>2016-10-31 09:10:12.2</date>"), field);
        assertEquals(expected, actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        Date expected = null;
        Field field = getClass().getField("testDate");
        ParseDate parseDate = new ParseDate();
        Date actual = parseDate.parse(ParserUtil.getParser("<date></date>"), field);
        assertEquals(expected, actual);
    }

    @Test
    public void parseSpace() throws Exception {
        Date expected = null;
        Field field = getClass().getField("testDate");
        ParseDate parseDate = new ParseDate();
        Date actual = parseDate.parse(ParserUtil.getParser("<date>   </date>"), field);
        assertEquals(expected, actual);
    }

    @Test
    public void parseNoFormat() throws Exception {
        Date expected = null;
        Field field = getClass().getField("testDateNoPattern");
        ParseDate parseDate = new ParseDate();
        Date actual = parseDate.parse(ParserUtil.getParser("<date>2016-10-31 09:10:12.2</date>"), field);
        assertEquals(expected, actual);
    }

    @Test
    public void parseWrongFormat() throws Exception {
        Date expected = null;
        Field field = getClass().getField("testDate");
        ParseDate parseDate = new ParseDate();
        Date actual = (Date) parseDate.parse(ParserUtil.getParser("<date>2016-10-3109:10:12</date>"), field);
        assertEquals(expected, actual);
    }

}