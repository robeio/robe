package io.robe.convert.excel;

import io.robe.convert.excel.importer.ExcelImporter;

import java.io.IOException;
import java.util.List;

public class ExcelImporterTest {
    @org.junit.Test
    public void testImportStream() {
        ExcelImporter excelImporter = new ExcelImporter();
        try {
            List<ExcelPojo> excelPojos = excelImporter.importXSLStream(ExcelPojo.class, ExcelImporterTest.class.getClassLoader().getResourceAsStream("excelMapping.xls"));

            for (ExcelPojo excelPojo : excelPojos) {
                System.out.println("-------------------------------------------");
                System.out.println("ID : " + excelPojo.getId());
                System.out.println("NAME : " + excelPojo.getName());
                System.out.println("SURNAME : " + excelPojo.getSurname());
                System.out.println("LONG_ID : " + excelPojo.getLongid());
                System.out.println("DOUBLE_ID : " + excelPojo.getDoubleid());
                System.out.println("BIG : " + excelPojo.getBig());
                System.out.println("DATE : " + excelPojo.getDate2());
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
