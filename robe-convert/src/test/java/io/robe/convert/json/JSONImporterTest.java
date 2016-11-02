package io.robe.convert.json;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
    public void testImportStreamWithHandler() throws Exception {
        JSONImporter<SamplePojo> importer = new JSONImporter<>(SamplePojo.class);
        InputStream stream = JSONImporterTest.class.getClassLoader().getResourceAsStream("sample.json");
        List<SamplePojo> items = new ArrayList<>();
        importer.importStream(stream, o -> items.add((SamplePojo) o));
        assertEquals(5, items.size());
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

    @Test(expected = Exception.class)
    public void testWithCorruptedJson() throws Exception {
        JSONImporter<SamplePojo> importer = new JSONImporter<>(SamplePojo.class);
        InputStream stream = new ByteArrayInputStream("{in".getBytes());
        importer.importStream(stream, o -> { });
    }

}
