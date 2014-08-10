package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.exporter.XLSXExporter;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class XLSXExporterTest {

    private String SAMPLE = "/Users/hasanmumin/Desktop/temp/1.xls";

    @Test
    public void exportStream() throws Exception {

        OutputStream outputStream = new FileOutputStream(new File(SAMPLE));
        XLSXExporter xlsxExporter = new XLSXExporter(true);
        xlsxExporter.exportStream(SamplePojo.class, outputStream, TestData.getData().iterator());

        outputStream.flush();

    }
}
