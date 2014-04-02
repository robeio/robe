package io.robe.convert.csv;


import io.robe.convert.SamplePojo;

import java.util.List;

public class CSVImporterTest {
    @org.junit.Test
    public void testImportStream() throws Exception {

        CSVImporter CSVImporter = new CSVImporter();
        List<SamplePojo> list = CSVImporter.importStream(SamplePojo.class, CSVImporterTest.class.getClassLoader().getResourceAsStream("sample.csv"));

        for (SamplePojo pojo : list) {
            System.out.println(pojo.toString());
        }


    }

}
