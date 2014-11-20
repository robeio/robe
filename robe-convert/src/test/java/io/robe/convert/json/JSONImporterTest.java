package io.robe.convert.json;

import io.robe.convert.SamplePojo;
import io.robe.convert.common.OnItemHandler;
import org.junit.Test;

import java.util.List;

public class JSONImporterTest {
    @Test
    public void testImportStream() throws Exception {


        JSONImporter<SamplePojo> importer = new JSONImporter(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(JSONImporterTest.class.getClassLoader().getResourceAsStream("sample.json"));

        for (SamplePojo pojo : list) {
            System.out.println(pojo.toString());
        }

    }

    @Test
    public void testImportStreamByItem() throws Exception {
        JSONImporter<SamplePojo> importer = new JSONImporter(SamplePojo.class);
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo samplePojo) {
                System.out.println(samplePojo.toString());
            }
        };
        importer.importStream(JSONImporterTest.class.getClassLoader().getResourceAsStream("sample.json"), handler);

    }
}
