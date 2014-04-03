package io.robe.convert.tsv;

import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class TSVExporterTest {
    @Test
    public void testExportStream() throws Exception {
        TSVExporter exporter = new TSVExporter();
        ArrayList<SamplePojo> pojos = new ArrayList<SamplePojo>(3);
        pojos.add(new SamplePojo(1, "Seray", "DDD", 1, 2, new BigDecimal("12.2"), new Date()));
        pojos.add(new SamplePojo(1, "Kaan", "BBB", 2, 3, BigDecimal.ONE, new Date()));
        pojos.add(new SamplePojo(1, "Sinan", "CCC", 3, 4, BigDecimal.ZERO, new Date()));


        OutputStream outputStream = System.out;

        exporter.exportStream(SamplePojo.class, outputStream, pojos);

        outputStream.close();
    }
}
