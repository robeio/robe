package io.robe.convert.excel;


import io.robe.convert.SamplePojo;
import io.robe.convert.excel.exporter.XLSExporter;
import io.robe.convert.excel.importer.XLSImporter;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class XLSExporterTest {
    @Test
    public void exportStream() throws Exception {
        XLSImporter xlsImporter = new XLSImporter(false);

        List<ArrayList> pojos = xlsImporter.importStream(SamplePojo.class, XLSImporterTest.class.getClassLoader().getResourceAsStream("sample.xls"));

        OutputStream outputStream = new FileOutputStream(XLSExporterTest.class.getClassLoader().getResource("sample.xls").getFile());
        XLSExporter xlsExporter = new XLSExporter(false);
        xlsExporter.exportStream(SamplePojo.class, outputStream, pojos);

        outputStream.close();

    }
}
