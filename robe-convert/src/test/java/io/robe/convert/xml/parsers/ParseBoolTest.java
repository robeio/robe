package io.robe.convert.xml.parsers;

import org.junit.Test;

import static io.robe.convert.xml.ParserUtil.getParser;
import static org.junit.Assert.assertEquals;

/**
 * Created by serayuzgur on 27/10/16.
 */
public class ParseBoolTest {

    @Test
    public void parseTrue() throws Exception {
        Boolean expected = true;
        ParseBool parseBool = new ParseBool();
        Boolean actual = (Boolean) parseBool.parse(getParser("<bool>true</bool>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseFalse() throws Exception {
        Boolean expected = false;
        ParseBool parseBool = new ParseBool();
        Boolean actual = (Boolean) parseBool.parse(getParser("<bool>false</bool>"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseEmpty() throws Exception {
        Boolean expected = false;
        ParseBool parseBool = new ParseBool();
        Boolean actual = (Boolean) parseBool.parse(getParser("<bool></bool>"), null);
        assertEquals(expected, actual);
    }

}