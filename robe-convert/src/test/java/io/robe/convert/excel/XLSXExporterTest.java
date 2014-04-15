package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.exporter.XLSXExporter;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class XLSXExporterTest {
    @Test
    public void exportStream() throws Exception {


        OutputStream outputStream = new FileOutputStream(XLSXExporterTest.class.getClassLoader().getResource("sample.xlsx").getFile());

        XLSXExporter xlsxExporter = new XLSXExporter(false);
        xlsxExporter.exportStream(SamplePojo.class, outputStream, TestData.getData().iterator());

        outputStream.flush();

    }
}
