package io.robe.convert.json;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.OutputStream;

public class JSONExporterTest {
    @Test
    public void testExportStream() throws Exception {
        JSONExporter exporter = new JSONExporter();


        OutputStream outputStream = System.out;

        exporter.exportStream(SamplePojo.class, outputStream, TestData.getData().iterator());
    }
}
