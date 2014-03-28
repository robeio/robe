package io.robe.convert.excel;


import io.robe.convert.SamplePojo;
import io.robe.convert.excel.exporter.XLSExporter;
import io.robe.convert.excel.importer.XLSImporter;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XLSExporterTest {
    @Test
    public void exportStream() {
        XLSImporter xlsImporter = new XLSImporter();

        List<ArrayList> pojos = null;
        try {
            pojos = xlsImporter.importStream(SamplePojo.class, XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        String yourPath = "";

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(yourPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            XLSExporter xlsExporter = new XLSExporter();
            xlsExporter.exportStream(SamplePojo.class, outputStream, pojos);
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
