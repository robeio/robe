package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hasanmumin on 01/11/2016.
 */
public class ParseCharTest {

    private ParseChar parseChar = new ParseChar();

    private Character expected = 'e';


    @Test
    public void parse() throws Exception {
        Character actual = parseChar.parse(null, null);

        assertEquals(null, actual);

        actual = parseChar.parse(expected, null);

        assertEquals(expected, actual);

    }

    @Test
    public void setCell() throws Exception {

        Cell cell = CellGenerateUtil.create();

        parseChar.setCell(null, cell, null);
        assertEquals("", cell.getStringCellValue());

        parseChar.setCell(expected, cell, null);
        assertEquals("e", cell.getStringCellValue());

    }

}