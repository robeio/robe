package io.robe.convert.tsv;

import io.robe.convert.OnItemHandler;
import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.util.List;

public class TSVImporterTest {
    @Test
    public void testImportStream() throws Exception {
        TSVImporter importer = new TSVImporter();
        List<SamplePojo> list = importer.importStream(SamplePojo.class, TSVImporterTest.class.getClassLoader().getResourceAsStream("sample.tsv"));

        for (SamplePojo pojo : list) {
            System.out.println(pojo.toString());
        }
    }

    @Test
    public void testImportStreamByItem() throws Exception {

        TSVImporter importer = new TSVImporter();
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo item) {
                System.out.println(item.toString());
            }
        };
        importer.importStream(SamplePojo.class, TSVImporterTest.class.getClassLoader().getResourceAsStream("sample.tsv"), handler);

    }
}
