package io.robe.convert.csv;

import io.robe.convert.FileUtil;
import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class CSVExporterTest {
    //@Test
    public void testExportStream() throws Exception {

        // Write to temp file
        File outputFile = FileUtil.getRandomTempFile();
        OutputStream outputStream = new FileOutputStream(outputFile);

        CSVExporter<SamplePojo> exporter = new CSVExporter(SamplePojo.class);
        exporter.exportStream(outputStream, TestData.getData().iterator());

        CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(new FileInputStream(outputFile.getPath()));
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo importedPojo : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
            System.out.println(ref);
        }

        outputFile.delete();
    }
}
