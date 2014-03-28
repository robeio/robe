package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.excel.importer.XLSXImporter;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class XLSXImporterTest {
    @Test
    public void testImportStream() {
        XLSXImporter xlsxImporter = new XLSXImporter();
        try {
            List<SamplePojo> samplePojos = xlsxImporter.importStream(SamplePojo.class, XLSXImporterTest.class.getClassLoader().getResourceAsStream("sample.xlsx"));

            for (SamplePojo samplePojo : samplePojos) {
                System.out.println("-------------------------------------------");
                System.out.println("ID : " + samplePojo.getId());
                System.out.println("NAME : " + samplePojo.getName());
                System.out.println("SURNAME : " + samplePojo.getSurname());
                System.out.println("LONG_ID : " + samplePojo.getLongid());
                System.out.println("DOUBLE_ID : " + samplePojo.getDoubleid());
                System.out.println("BIG : " + samplePojo.getBig());
                System.out.println("DATE : " + samplePojo.getDate2());
                System.out.println("-------------------------------------------");
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
