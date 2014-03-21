package io.robe.convert;

import io.robe.convert.pojo.CSVPojo;

import java.io.IOException;
import java.util.List;

public class CSVImporterTest {
    @org.junit.Test
    public void testImportStream() throws Exception {

        CSVImporter CSVImporter = new CSVImporter();
        try {
            List<Object> l = CSVImporter.importStream(CSVImporterTest.class.getClassLoader().getResourceAsStream("csvMapping.csv"), CSVPojo.class);
            System.out.println(((CSVPojo)l.get(0)).getNameSurname());
            System.out.println(((CSVPojo)l.get(1)).getNameSurname());
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
