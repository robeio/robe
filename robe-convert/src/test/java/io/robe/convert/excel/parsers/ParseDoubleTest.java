package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ParseDoubleTest {

    private ParseDouble parseDouble = new ParseDouble();

    private Double expected = 123.12d;

    @Test
    public void parse() throws Exception {
        Double actual = (Double) parseDouble.parse(null, null);
        assertEquals(null, actual);

        actual = (Double) parseDouble.parse("123.12", null);
        assertEquals(expected, actual);
    }

    @Test
    public void setCell() throws Exception {

        Cell cell = CellGenerateUtil.create();

        parseDouble.setCell(null, cell, null);
        assertEquals("", cell.getStringCellValue());
        parseDouble.setCell(expected, cell, null);
        assertEquals(expected, cell.getNumericCellValue(), 0.01d);

    }

}