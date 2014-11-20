package io.robe.convert.csv;


import io.robe.convert.SamplePojo;
import io.robe.convert.common.OnItemHandler;
import org.junit.Test;

import java.util.List;

public class CSVImporterTest {
    @Test
    public void testImportStream() throws Exception {

        CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(CSVImporterTest.class.getClassLoader().getResourceAsStream("sample.csv"));

        for (SamplePojo pojo : list) {
            System.out.println(pojo.toString());
        }
    }


    @Test
    public void testImportStreamByItem() throws Exception {

        CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo item) {
                System.out.println(item.toString());
            }
        };
        importer.importStream(CSVImporterTest.class.getClassLoader().getResourceAsStream("sample.csv"), handler);

    }

}
