package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseStringTest {

    private String expected = "robe";
    private ParseString parseString = new ParseString();

    @Test
    public void parse() throws Exception {

        String actual = parseString.parse(null, null);

        assertEquals(null, actual);

        actual = parseString.parse(expected, null);
        assertEquals(expected, actual);
    }

    @Test
    public void setCell() throws Exception {
        Cell cell = CellGenerateUtil.create();

        parseString.setCell(null, cell, null);

        assertEquals("", cell.getStringCellValue());

        parseString.setCell(expected, cell, null);

        assertEquals(expected, cell.getStringCellValue());
    }

}