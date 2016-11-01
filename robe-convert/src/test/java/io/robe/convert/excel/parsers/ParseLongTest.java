package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hasanmumin on 01/11/2016.
 */
public class ParseLongTest {

    private ParseLong parseLong = new ParseLong();

    private String expected = "2.1";

    @Test
    public void parse() throws Exception {
        Long actual = parseLong.parse(null, null);
        assertEquals(null, actual);

        actual = parseLong.parse(expected, null);
        assertEquals(Double.valueOf(expected).longValue(), actual, 0);
    }

    @Test
    public void setCell() throws Exception {
        Cell cell = CellGenerateUtil.create();

        parseLong.setCell(null, cell, null);
        assertEquals("", cell.getStringCellValue());

        parseLong.setCell(Double.valueOf(expected).longValue(), cell, null);
        assertEquals(Double.valueOf(expected).longValue(), cell.getNumericCellValue(), 0);


    }

}