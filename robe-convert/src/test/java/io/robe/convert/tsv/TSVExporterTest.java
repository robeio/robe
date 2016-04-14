package io.robe.convert.tsv;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class TSVExporterTest {
    @Test
    public void testExportStream() throws Exception {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TSVExporter<SamplePojo> exporter = new TSVExporter(SamplePojo.class);
        exporter.exportStream(os, TestData.getData().iterator());
        os.flush();
        os.close();
        TSVImporter<SamplePojo> importer = new TSVImporter<>(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(new ByteArrayInputStream(os.toByteArray()));
        assert list.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }
}
