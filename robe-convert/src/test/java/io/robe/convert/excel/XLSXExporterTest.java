package io.robe.convert.excel;

import io.robe.convert.FileUtil;
import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.exporter.XLSXExporter;
import io.robe.convert.excel.importer.XLSXImporter;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class XLSXExporterTest {

    @Test
    public void exportStream() throws Exception {

        // Write to temp file
        File outputFile = FileUtil.getRandomTempFile();
        OutputStream outputStream = new FileOutputStream(outputFile);
        XLSXExporter<SamplePojo> xlsxExporter = new XLSXExporter(SamplePojo.class, false);
        xlsxExporter.exportStream(outputStream, TestData.getData().iterator());
        outputStream.flush();
        outputStream.close();

        XLSXImporter<SamplePojo> xlsImporter = new XLSXImporter(SamplePojo.class, false);
        List<SamplePojo> list = xlsImporter.importStream(new FileInputStream(outputFile.getPath()));
        assert list.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
            System.out.println(ref);
        }

        outputFile.delete();
    }

    @Test
    public void exportStreamWithTitle() throws Exception {
        // Write to temp file
        File outputFile = FileUtil.getRandomTempFile();
        OutputStream outputStream = new FileOutputStream(outputFile);
        XLSXExporter<SamplePojo> xlsxExporter = new XLSXExporter(SamplePojo.class, true);
        xlsxExporter.exportStream(outputStream, TestData.getData().iterator());
        outputStream.flush();
        outputStream.close();

        XLSXImporter<SamplePojo> xlsxImporter = new XLSXImporter(SamplePojo.class, true);
        List<SamplePojo> samplePojos = xlsxImporter.importStream(new FileInputStream(outputFile.getPath()));
        assert samplePojos.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo importedPojo : samplePojos) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
            System.out.println(ref);
        }
        outputFile.delete();
    }
}
