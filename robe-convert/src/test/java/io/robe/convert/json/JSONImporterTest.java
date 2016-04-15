package io.robe.convert.json;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JSONImporterTest {
    private static final String ENCODING_UTF_16 = "UTF-16LE";

    @Test
    public void testImportStream() throws Exception {

        JSONImporter<SamplePojo> importer = new JSONImporter(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(JSONImporterTest.class.getClassLoader().getResourceAsStream("sample.json"));

        assert list.size() == TestData.getData().size();
        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assertEquals(ref, item);
        }
    }

    @Test
    public void testImportStreamWithEncoding() throws Exception {

        JSONImporter<SamplePojo> importer = new JSONImporter(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(JSONImporterTest.class.getClassLoader().getResourceAsStream("sampleUTF16.json"), ENCODING_UTF_16);

        assert list.size() == TestData.getData().size();
        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assertEquals(ref, item);
        }
    }
}
