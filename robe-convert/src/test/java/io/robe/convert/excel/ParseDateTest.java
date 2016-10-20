package io.robe.convert.excel;

import io.robe.convert.excel.importer.XLSImporter;
import io.robe.convert.excel.parsedate.ParseDateSamplePojo;
import io.robe.convert.excel.parsedate.ParseDateSamplePojo1;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 20/10/2016.
 */
public class ParseDateTest {

    @Test
    public void parseShouldThrowRuntimeException() {
        XLSImporter<ParseDateSamplePojo> xlsImporter = new XLSImporter(ParseDateSamplePojo.class, false);
        try {
            xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("ParseDateTest-sample1.xls"));
            Assert.fail("Should be JsonFormat Exception");
        } catch (Exception e) {
            Assert.assertTrue("JsonFormat with pattern needed for: date".equals(e.getMessage()));
        }
    }

    @Test
    public void parseShouldThrowParseException() {
        XLSImporter<ParseDateSamplePojo1> xlsImporter = new XLSImporter(ParseDateSamplePojo1.class, false);
        try {
            xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("ParseDateTest-sample1.xls"));
            Assert.fail("Should be ParseException Exception");
        } catch (Exception e) {
            Assert.assertTrue("Wrong pattern exception", true);
        }
    }

}
