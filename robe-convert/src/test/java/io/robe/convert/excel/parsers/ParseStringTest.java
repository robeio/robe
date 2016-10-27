package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hasanmumin on 27/10/2016.
 */
public class ParseStringTest {

    private Object expected = "robe";
    private ParseString parseString = new ParseString();

    @Test
    public void parse() throws Exception {

        String actual = (String) parseString.parse(null, null);

        assertEquals(null, actual);

        actual = (String) parseString.parse(expected, null);
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