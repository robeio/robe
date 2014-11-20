package io.robe.convert.json;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.util.List;

public class JSONImporterTest {
    @Test
    public void testImportStream() throws Exception {

        JSONImporter<SamplePojo> importer = new JSONImporter(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(JSONImporterTest.class.getClassLoader().getResourceAsStream("sample.json"));

        assert list.size() == TestData.getData().size();
        int index = 0;
        for (SamplePojo importedPojo : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert importedPojo.equals(ref);
            System.out.println(ref);
        }
    }
}
