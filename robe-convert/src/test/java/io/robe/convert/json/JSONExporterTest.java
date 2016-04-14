package io.robe.convert.json;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class JSONExporterTest {


    @Test
    public void testExportStream() throws Exception {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JSONExporter<SamplePojo> exporter = new JSONExporter(SamplePojo.class);
        exporter.exportStream(os, TestData.getData().iterator());
        os.flush();
        os.close();
        JSONImporter<SamplePojo> importer = new JSONImporter(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(new ByteArrayInputStream(os.toByteArray()));
        assert list.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
        }
    }
}
