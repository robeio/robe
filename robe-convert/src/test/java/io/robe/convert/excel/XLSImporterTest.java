package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.importer.XLSImporter;

import java.util.List;

public class XLSImporterTest {
    @org.junit.Test
    public void testImportStream() throws Exception {
        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class, false);
        List<SamplePojo> samplePojos = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"));
        assert samplePojos.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo importedPojo : samplePojos) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
            System.out.println(ref);
        }
    }

    @org.junit.Test
    public void testImportStreamWithTitle() throws Exception {
        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class, true);
        List<SamplePojo> samplePojos = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sampleWithTitle.xls"));
        assert samplePojos.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo importedPojo : samplePojos) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
            System.out.println(ref);
        }
    }
}
