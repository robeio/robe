package io.robe.convert.xml;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

public class XMLExporterTest {
    @Test
    public void testExportStream() throws Exception {

        XMLExporter<SamplePojo> exporter = new XMLExporter<>(SamplePojo.class);

        exporter.exportStream(System.out, TestData.getData().iterator());

    }
}
