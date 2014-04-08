package io.robe.convert.excel;

import io.robe.convert.OnItemHandler;
import io.robe.convert.SamplePojo;
import io.robe.convert.excel.importer.XLSImporter;

import java.util.List;

public class XLSImporterTest {
    @org.junit.Test
    public void testImportStream() throws Exception {
        XLSImporter xlsImporter = new XLSImporter(false);
        List<SamplePojo> samplePojos = xlsImporter.importStream(SamplePojo.class, XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"));

        for (SamplePojo samplePojo : samplePojos) {
            System.out.println(samplePojo.toString());
        }

    }

    @org.junit.Test
    public void testImportStreamBtItem() throws Exception {
        XLSImporter xlsImporter = new XLSImporter(false);
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo samplePojo) {
                System.out.println(samplePojo.toString());
            }
        };
        xlsImporter.importStream(SamplePojo.class, XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"), handler);
    }
}
