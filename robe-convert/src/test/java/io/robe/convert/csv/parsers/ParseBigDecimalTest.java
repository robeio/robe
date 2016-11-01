package io.robe.convert.csv.parsers;

import org.junit.Test;

import java.text.DecimalFormatSymbols;

import org.supercsv.cellprocessor.Optional;

import static org.junit.Assert.*;


public class ParseBigDecimalTest {
    @Test
    public void constructorTests() throws Exception {
        new ParseBigDecimal(DecimalFormatSymbols.getInstance());
        new ParseBigDecimal(new Optional());
        new ParseBigDecimal(DecimalFormatSymbols.getInstance(), new Optional());
        //No exception means success.
    }

}