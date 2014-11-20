package io.robe.convert.tsv;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.OutputStream;

public class TSVExporterTest {
    @Test
    public void testExportStream() throws Exception {
        TSVExporter<SamplePojo> exporter = new TSVExporter(SamplePojo.class);

        OutputStream outputStream = System.out;

        exporter.exportStream(outputStream, TestData.getData().iterator());

    }
}
