package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseByteTest {

    private ParseByte parseByte = new ParseByte();

    private String expected = "100";

    @Test
    public void parse() throws Exception {

        Byte actual = (Byte) parseByte.parse(null, null);

        assertEquals(null, actual);

        actual = (Byte) parseByte.parse(expected, null);

        assertEquals(Byte.valueOf(expected), actual);

    }

    @Test
    public void setCell() throws Exception {

        Cell cell = CellGenerateUtil.create();

        parseByte.setCell(null, cell, null);

        assertEquals("", cell.getStringCellValue());

        parseByte.setCell(Byte.valueOf(expected), cell, null);

        double result = Double.valueOf(expected);

        assertEquals(result, cell.getNumericCellValue(), 0.01d);

    }

}