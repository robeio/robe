package io.robe.convert;


import io.robe.convert.csv.CSVImporter;

import java.io.IOException;
import java.util.List;

public class CSVImporterTest {
    @org.junit.Test
    public void testImportStream() throws Exception {

        CSVImporter CSVImporter = new CSVImporter();
        try {
            List<SamplePojo> list = CSVImporter.importStream( SamplePojo.class,CSVImporterTest.class.getClassLoader().getResourceAsStream("sample.csv"));

            for(SamplePojo pojo : list){
                System.out.println(pojo.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
