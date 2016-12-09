package io.robe.convert.excel.parsers;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.robe.convert.excel.CellGenerateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParseDateTest {

    private ParseDate parseDate = new ParseDate();

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date jsonFormatField;
    private Date noneJsonFormatField;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date wrongFormatField;

    private String expected = "31.10.2016";

    @Test
    public void parse() throws Exception {
        Date actual =  parseDate.parse(null, null);
        assertEquals(null, actual);
        actual =  parseDate.parse(expected, this.getClass().getDeclaredField("jsonFormatField"));
        assertEquals(new SimpleDateFormat("dd.MM.yyyy").parse(expected), actual);
    }

    @Test(expected = RuntimeException.class)
    public void parseWithoutJsonFormat() throws Exception {
        parseDate.parse(expected, this.getClass().getDeclaredField("noneJsonFormatField"));
        fail("Should throw RuntimeException");
    }

    @Test(expected = RuntimeException.class)
    public void parseWithWrongJsonFormat() throws Exception {
        parseDate.parse(expected, this.getClass().getDeclaredField("wrongFormatField"));
        fail("Should throw RuntimeException");
    }

    @Test
    public void setCell() throws Exception {
        Cell cell = CellGenerateUtil.create();

        parseDate.setCell(null, cell, null);

        assertEquals("", cell.getStringCellValue());

        Date actual = new Date();
        parseDate.setCell(new Date(), cell, this.getClass().getDeclaredField("jsonFormatField"));
        assertEquals(new SimpleDateFormat("dd.MM.yyyy").format(actual), cell.getStringCellValue());
    }
}