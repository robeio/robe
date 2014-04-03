package io.robe.convert.tsv;

import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.util.List;

public class TSVImporterTest {
    @Test
    public void testImportStream() throws Exception {


        TSVImporter CSVImporter = new TSVImporter();
        List<SamplePojo> list = CSVImporter.importStream(SamplePojo.class, TSVImporterTest.class.getClassLoader().getResourceAsStream("sample.tsv"));

        for (SamplePojo pojo : list) {
            System.out.println(pojo.toString());
        }

    }
}
