package io.robe.convert.csv;


import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.common.OnItemHandler;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class CSVImporterTest {

    private static final String ENCODING_UTF_16 = "UTF-16LE";

    @Test
    public void testImportStream() throws Exception {

        CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(CSVImporterTest.class.getClassLoader().getResourceAsStream("sample.csv"));
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo importedPojo : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
        }
    }

    @Test
    public void testImportStreamHandler() throws Exception {

        CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
        List<SamplePojo> list = new LinkedList<>();
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo samplePojo) throws Exception {
                list.add(samplePojo);
            }
        };
        importer.importStream(CSVImporterTest.class.getClassLoader().getResourceAsStream("sample.csv"),handler);
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo importedPojo : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testImportStreamError() throws Exception {

        CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
        importer.importStream(null);
    }

    @Test
    public void testImportStreamWithEncoding() throws Exception {

        CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(CSVImporterTest.class.getClassLoader().getResourceAsStream("sampleUTF16.csv"), ENCODING_UTF_16);
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo importedPojo : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
        }
    }

}
