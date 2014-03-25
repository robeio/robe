package io.robe.convert;


import io.robe.convert.csv.CSVImporter;

import java.io.IOException;
import java.util.List;

public class CSVImporterTest {
    @org.junit.Test
    public void testImportStream() throws Exception {

        CSVImporter CSVImporter = new CSVImporter();
        try {
            List<CSVPojo> l = CSVImporter.importStream( CSVPojo.class,CSVImporterTest.class.getClassLoader().getResourceAsStream("csvMapping.csv"));

            for(CSVPojo pojo : l){
                System.out.println(pojo.getDate2());
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
