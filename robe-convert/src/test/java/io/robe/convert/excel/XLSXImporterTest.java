package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.importer.XLSXImporter;

import java.util.List;

public class XLSXImporterTest {


    @org.junit.Test
    public void testImportStream() throws Exception {
        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class, false);
        List<SamplePojo> samplePojos = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"));
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
        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class, true);
        List<SamplePojo> samplePojos = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sampleWithTitle.xlsx"));
        assert samplePojos.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo importedPojo : samplePojos) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
            System.out.println(ref);
        }
    }
}
