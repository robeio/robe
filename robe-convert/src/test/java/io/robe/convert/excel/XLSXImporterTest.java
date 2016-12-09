package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.common.OnItemHandler;
import io.robe.convert.excel.importer.XLSXImporter;

import java.util.List;

public class XLSXImporterTest {


    @org.junit.Test
    public void testImportStream() throws Exception {
        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class, false);
        List<SamplePojo> list = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"));
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }

    @org.junit.Test
    public void testImportStreamWithTitle() throws Exception {
        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class, true);
        List<SamplePojo> list = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sampleWithTitle.xlsx"));
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }


    @org.junit.Test
    public void testImportStreamWithCharset() throws Exception {
        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class);
        List<SamplePojo> list = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"), "UTF-8");
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }

    @org.junit.Test
    public void testImportStreamWithOnItemHandler() throws Exception {
        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class);

        final int[] index = {0};
        OnItemHandler<SamplePojo> onItemHandler = samplePojo -> {
            SamplePojo ref = TestData.getData().get(index[0]++);
            assert samplePojo.equals(ref);
        };

        xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"), onItemHandler);
    }

    @org.junit.Test
    public void testImportStreamWithOnItemHandlerWithCharset() throws Exception {
        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class);

        final int[] index = {0};
        OnItemHandler<SamplePojo> onItemHandler = samplePojo -> {
            SamplePojo ref = TestData.getData().get(index[0]++);
            assert samplePojo.equals(ref);
        };

        xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"), onItemHandler, "UTF-8");
    }
}
