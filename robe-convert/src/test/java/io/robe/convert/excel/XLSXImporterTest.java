package io.robe.convert.excel;

import io.robe.convert.OnItemHandler;
import io.robe.convert.SamplePojo;
import io.robe.convert.excel.importer.XLSXImporter;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class XLSXImporterTest {


    private String SAMPLE = XLSXExporterTest.class.getClassLoader().getResource("sample.xlsx").getFile();

    @Test
    public void testImportStream() throws Exception {
        XLSXImporter importer = new XLSXImporter(true);

        InputStream inputStream = new FileInputStream(new File(SAMPLE));
        List<SamplePojo> samplePojos = importer.importStream(SamplePojo.class, inputStream);

        for (SamplePojo samplePojo : samplePojos) {
            System.out.println(samplePojo.toString());
        }
    }

    @Test
    public void testImportStreamByItem() throws Exception {
        XLSXImporter importer = new XLSXImporter(true);
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo samplePojo) {
                System.out.println(samplePojo.toString());
            }
        };

        importer.importStream(SamplePojo.class, XLSXImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"), handler);

    }
}
