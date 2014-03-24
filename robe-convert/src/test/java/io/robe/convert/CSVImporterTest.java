package io.robe.convert;


import java.io.IOException;
import java.util.List;

public class CSVImporterTest {
    @org.junit.Test
    public void testImportStream() throws Exception {

        CSVImporter CSVImporter = new CSVImporter();
        try {
            List<CSVPojo> l = CSVImporter.importStream(CSVImporterTest.class.getClassLoader().getResourceAsStream("csvMapping.csv"), CSVPojo.class);

            for(CSVPojo pojo : l){
                System.out.println(pojo.getName());
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
