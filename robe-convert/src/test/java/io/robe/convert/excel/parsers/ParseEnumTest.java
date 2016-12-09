package io.robe.convert.excel.parsers;

import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by hasanmumin on 01/11/2016.
 */
public class ParseEnumTest {

    private ParseEnum parseEnum = new ParseEnum();

    private SAMPLE_ENUM sampleEnum;

    private String declaredField;

    @Test
    public void parse() throws Exception {
        Enum actual = parseEnum.parse(null, this.getClass().getDeclaredField("sampleEnum"));
        assertEquals(null, actual);

        actual = parseEnum.parse("FIRST", this.getClass().getDeclaredField("sampleEnum"));
        assertEquals(SAMPLE_ENUM.FIRST, actual);
    }

    @Test
    public void setCell() throws Exception {
        Cell cell = CellGenerateUtil.create();

        parseEnum.setCell(null, cell, null);
        assertEquals("", cell.getStringCellValue());

        parseEnum.setCell(SAMPLE_ENUM.SECOND, cell, null);
        assertEquals(SAMPLE_ENUM.SECOND.name(), cell.getStringCellValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseWithWrongField() throws NoSuchFieldException {
        parseEnum.parse("SECOND", this.getClass().getDeclaredField("declaredField"));
        fail("should be throw IllegalArgumentException");
    }


    private enum SAMPLE_ENUM {
        FIRST,
        SECOND
    }

}