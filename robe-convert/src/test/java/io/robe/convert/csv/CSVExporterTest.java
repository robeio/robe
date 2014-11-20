package io.robe.convert.csv;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.OutputStream;

public class CSVExporterTest {
    @Test
    public void testExportStream() throws Exception {

        CSVExporter<SamplePojo> exporter = new CSVExporter(SamplePojo.class);

        OutputStream outputStream = System.out;

        exporter.exportStream(outputStream, TestData.getData().iterator());
    }
}
