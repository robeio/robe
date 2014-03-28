package io.robe.convert.csv;

import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class CSVExporterTest {
    @Test
    public void testExportStream() throws Exception {

        CSVExporter exporter = new CSVExporter();
        ArrayList<SamplePojo> pojos = new ArrayList<SamplePojo>(3);
        pojos.add(new SamplePojo(1, "Seray", "DDD", 1, 2, new BigDecimal("12.2"), new Date()));
        pojos.add(new SamplePojo(1, "Kaan", "BBB", 2, 3, BigDecimal.ONE, new Date()));
        pojos.add(new SamplePojo(1, "Sinan", "CCC", 3, 4, BigDecimal.ZERO, new Date()));

        String yourPath = null;

        OutputStream outputStream = new FileOutputStream(new File(yourPath));

        exporter.exportStream(SamplePojo.class, outputStream, pojos);

        outputStream.close();
    }
}
