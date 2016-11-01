package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hasanmumin on 01/11/2016.
 */
public class ParseIntTest {

    private ParseInt parseInt = new ParseInt();
    private Integer expected = 1;
    private Double expectedWithDot = 2.1;

    @Test
    public void parse() throws Exception {

        Integer actual = parseInt.parse(null, null);
        assertEquals(null, actual);

        actual = parseInt.parse(expected, null);
        assertEquals(expected, actual);

        actual = parseInt.parse(expectedWithDot, null);
        assertEquals(Integer.valueOf(expectedWithDot.intValue()), actual);

    }

    @Test
    public void setCell() throws Exception {
        Cell cell = CellGenerateUtil.create();
        parseInt.setCell(null, cell, null);
        assertEquals("", cell.getStringCellValue());

        parseInt.setCell(expected, cell, null);
        assertEquals(Double.valueOf(expected.toString()), cell.getNumericCellValue(), 0.01);
    }

}