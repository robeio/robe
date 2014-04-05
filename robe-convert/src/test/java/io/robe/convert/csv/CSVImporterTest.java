package io.robe.convert.csv;


import io.robe.convert.OnItemHandler;
import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.util.List;

public class CSVImporterTest {
    @Test
    public void testImportStream() throws Exception {

        CSVImporter importer = new CSVImporter();
        List<SamplePojo> list = importer.importStream(SamplePojo.class, CSVImporterTest.class.getClassLoader().getResourceAsStream("sample.csv"));

        for (SamplePojo pojo : list) {
            System.out.println(pojo.toString());
        }
    }


    @Test
    public void testImportStreamByItem() throws Exception {

        CSVImporter importer = new CSVImporter();
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo item) {
                System.out.println(item.toString());
            }
        };
        importer.importStream(SamplePojo.class, CSVImporterTest.class.getClassLoader().getResourceAsStream("sample.csv"), handler);

    }

}
