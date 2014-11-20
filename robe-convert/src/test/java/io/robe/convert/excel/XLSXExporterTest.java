package io.robe.convert.excel;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.exporter.XLSXExporter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class XLSXExporterTest {

    private String SAMPLE = XLSXExporterTest.class.getClassLoader().getResource("sample.xlsx").getFile();
    private static final Logger LOGGER = LoggerFactory.getLogger(XLSXExporterTest.class);

    @Test
    public void exportStream() throws Exception {
        File exportFile = new File(SAMPLE);
        OutputStream outputStream = new FileOutputStream(exportFile);
        LOGGER.info(" New excel file exported to this location : " + exportFile.getCanonicalPath() +
                " with size : " + exportFile.getTotalSpace() + " bytes");

        XLSXExporter<SamplePojo> xlsxExporter = new XLSXExporter(SamplePojo.class, true);
        xlsxExporter.exportStream(outputStream, TestData.getData().iterator());

        outputStream.flush();

    }
}
