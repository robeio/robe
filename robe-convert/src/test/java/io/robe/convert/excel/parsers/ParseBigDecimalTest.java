package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by hasanmumin on 31/10/2016.
 */
public class ParseBigDecimalTest {

    private ParseBigDecimal parseBigDecimal = new ParseBigDecimal();

    private BigDecimal expected = new BigDecimal("123.12");

    @Test
    public void parse() throws Exception {
        BigDecimal actual = parseBigDecimal.parse(null, null);
        assertEquals(null, actual);
        actual = parseBigDecimal.parse("123.12", null);
        assertEquals(expected, actual);
    }

    @Test
    public void setCell() throws Exception {

        Cell cell = CellGenerateUtil.create();

        parseBigDecimal.setCell(null, cell, null);
        assertEquals("", cell.getStringCellValue());

        parseBigDecimal.setCell(expected, cell, null);
        assertEquals(cell.getNumericCellValue(), expected.doubleValue(), 0.01d);

    }

}