package io.robe.convert.xml;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XMLExporterTest {
    @Test
    public void testExportStream() throws Exception {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLExporter<SamplePojo> exporter = new XMLExporter<>(SamplePojo.class);
        exporter.exportStream(os, TestData.getData().iterator());
        os.flush();
        os.close();
        XMLImporter<SamplePojo> importer = new XMLImporter<>(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(new ByteArrayInputStream(os.toByteArray()));
        assert list.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assertEquals(ref,item);
        }
    }
}
