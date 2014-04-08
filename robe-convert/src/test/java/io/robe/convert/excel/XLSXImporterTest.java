package io.robe.convert.excel;

import io.robe.convert.OnItemHandler;
import io.robe.convert.SamplePojo;
import io.robe.convert.excel.importer.XLSXImporter;
import org.junit.Test;

import java.util.List;

public class XLSXImporterTest {
    @Test
    public void testImportStream() throws Exception {
        XLSXImporter importer = new XLSXImporter();
        List<SamplePojo> samplePojos = importer.importStream(SamplePojo.class, XLSXImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"));

        for (SamplePojo samplePojo : samplePojos) {
            System.out.println(samplePojo.toString());
        }
    }

    @Test
    public void testImportStreamByItem() throws Exception {
        XLSXImporter importer = new XLSXImporter();
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo samplePojo) {
                System.out.println(samplePojo.toString());
            }
        };

        importer.importStream(SamplePojo.class, XLSXImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"), handler);

    }
}
