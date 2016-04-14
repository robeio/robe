package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.exporter.XLSXExporter;
import io.robe.convert.excel.importer.XLSXImporter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class XLSXExporterTest {

    @Test
    public void exportStream() throws Exception {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XLSXExporter<SamplePojo> xlsxExporter = new XLSXExporter(SamplePojo.class, false);
        xlsxExporter.exportStream(os, TestData.getData().iterator());
        os.flush();
        os.close();

        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class, false);
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
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XLSXExporter<SamplePojo> xlsxExporter = new XLSXExporter(SamplePojo.class, true);
        xlsxExporter.exportStream(os, TestData.getData().iterator());
        os.flush();
        os.close();

        XLSXImporter<SamplePojo> xlsxImporter = new XLSXImporter(SamplePojo.class, true);
        List<SamplePojo> samplePojos = xlsxImporter.importStream(new ByteArrayInputStream(os.toByteArray()));
        assert samplePojos.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo importedPojo : samplePojos) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
        }
    }
}
