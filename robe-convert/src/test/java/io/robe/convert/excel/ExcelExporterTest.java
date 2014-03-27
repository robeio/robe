package io.robe.convert.excel;


import io.robe.convert.SamplePojo;
import io.robe.convert.csv.CSVExporter;
import io.robe.convert.excel.exporter.ExcelExporter;
import io.robe.convert.excel.importer.ExcelImporter;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelExporterTest {
    @Test
    public void excelExporter() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        ExcelImporter excelImporter = new ExcelImporter();

        List<ArrayList> pojos = excelImporter.importXSLStream(ExcelPojo.class, ExcelImporterTest.class.getClassLoader().getResourceAsStream("excelMapping.xls"));

        String yourPath = "";

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(yourPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ExcelExporter excelExporter = new ExcelExporter();
            excelExporter.exportXSLStream(SamplePojo.class, outputStream, pojos);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
