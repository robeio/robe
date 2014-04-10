package io.robe.convert.tsv;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.OutputStream;

public class TSVExporterTest {
    @Test
    public void testExportStream() throws Exception {
        TSVExporter exporter = new TSVExporter();

        OutputStream outputStream = System.out;

        exporter.exportStream(SamplePojo.class, outputStream, TestData.getData().iterator());

    }
}
