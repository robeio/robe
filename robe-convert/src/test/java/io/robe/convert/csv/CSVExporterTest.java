package io.robe.convert.csv;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.OutputStream;

public class CSVExporterTest {
    @Test
    public void testExportStream() throws Exception {

        CSVExporter exporter = new CSVExporter();


        OutputStream outputStream = System.out;

        exporter.exportStream(SamplePojo.class, outputStream, TestData.getData().iterator());

//        outputStream.flush();
    }
}
