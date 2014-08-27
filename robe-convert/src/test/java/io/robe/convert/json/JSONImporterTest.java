package io.robe.convert.json;

import io.robe.convert.OnItemHandler;
import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.util.List;

public class JSONImporterTest {
    @Test
    public void testImportStream() throws Exception {


        JSONImporter importer = new JSONImporter();
        List<SamplePojo> list = importer.<SamplePojo>importStream(SamplePojo.class, JSONImporterTest.class.getClassLoader().getResourceAsStream("sample.json"));

        for (SamplePojo pojo : list) {
            System.out.println(pojo.toString());
        }

    }

    @Test
    public void testImportStreamByItem() throws Exception {
        JSONImporter importer = new JSONImporter();
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo samplePojo) {
                System.out.println(samplePojo.toString());
            }
        };
        importer.<SamplePojo>importStream(SamplePojo.class, JSONImporterTest.class.getClassLoader().getResourceAsStream("sample.json"), handler);

    }
}
