package io.robe.convert.csv.parsers;

import org.junit.Test;
import org.supercsv.cellprocessor.Optional;

import java.util.Locale;



public class ParseDateTest {
    @Test
    public void constructorsTest() throws Exception {
        new ParseDate("yyyyMMddHHmm");
        new ParseDate("yyyyMMddHHmm", true);
        new ParseDate("yyyyMMddHHmm", true, Locale.getDefault());
        new ParseDate("yyyyMMddHHmm", new Optional());
        new ParseDate("yyyyMMddHHmm", true, new Optional());
        new ParseDate("yyyyMMddHHmm", true, Locale.getDefault(), new Optional());
        //No exception means success.
    }

}