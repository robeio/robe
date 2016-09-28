package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.common.OnItemHandler;
import io.robe.convert.excel.importer.XLSImporter;

import java.util.List;

public class XLSImporterTest {
    @org.junit.Test
    public void testImportStream() throws Exception {
        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class, false);
        List<SamplePojo> list = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"));
        assert list.size() == TestData.getData().size();
        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }

    @org.junit.Test
    public void testImportStreamWithTitle() throws Exception {
        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class, true);
        List<SamplePojo> list = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sampleWithTitle.xls"));
        assert list.size() == TestData.getData().size();
        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }

    @org.junit.Test
    public void testImportStreamWithCharset() throws Exception {
        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class);
        List<SamplePojo> list = xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"), "UTF-8");
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }

    @org.junit.Test
    public void testImportStreamWithOnItemHandler() throws Exception {
        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class);

        final int[] index = {0};
        OnItemHandler<SamplePojo> onItemHandler = samplePojo -> {
            SamplePojo ref = TestData.getData().get(index[0]++);
            assert samplePojo.equals(ref);
        };

        xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"), onItemHandler);
    }

    @org.junit.Test
    public void testImportStreamWithOnItemHandlerWithCharset() throws Exception {
        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class);

        final int[] index = {0};
        OnItemHandler<SamplePojo> onItemHandler = samplePojo -> {
            SamplePojo ref = TestData.getData().get(index[0]++);
            assert samplePojo.equals(ref);
        };

        xlsImporter.importStream(XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"), onItemHandler, "UTF-8");
    }
}
