package io.robe.convert.excel;


import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.exporter.XLSExporter;
import io.robe.convert.excel.importer.XLSImporter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class XLSExporterTest {


    @Test
    public void exportStream() throws Exception {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XLSExporter<SamplePojo> xlsExporter = new XLSExporter(SamplePojo.class, false);
        xlsExporter.exportStream(os, TestData.getData().iterator());
        os.flush();
        os.close();

        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class, false);
        List<SamplePojo> list = xlsImporter.importStream(new ByteArrayInputStream(os.toByteArray()));
        assert list.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }

    @Test
    public void exportStreamWithTitle() throws Exception {
        // Write to temp file
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XLSExporter<SamplePojo> xlsExporter = new XLSExporter(SamplePojo.class, true);
        xlsExporter.exportStream(os, TestData.getData().iterator());
        os.flush();
        os.close();

        XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class, true);
        List<SamplePojo> list = xlsImporter.importStream(new ByteArrayInputStream(os.toByteArray()));
        assert list.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }
}
