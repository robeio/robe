package io.robe.convert.csv;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class CSVExporterTest {
    @Test
    public void testExportStream() throws Exception {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CSVExporter<SamplePojo> exporter = new CSVExporter(SamplePojo.class);
        exporter.exportStream(os, TestData.getData().iterator());
        os.flush();
        os.close();

        CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(new ByteArrayInputStream(os.toByteArray()));
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo importedPojo : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
        }
    }
}
