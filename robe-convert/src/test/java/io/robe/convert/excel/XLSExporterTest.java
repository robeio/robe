package io.robe.convert.excel;


import io.robe.convert.FileUtil;
import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.excel.exporter.XLSExporter;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class XLSExporterTest {


    @Test
    public void exportStream() throws Exception {

        File outputFile = FileUtil.getRandomTempFile();
        OutputStream outputStream = new FileOutputStream(outputFile);
        XLSExporter<SamplePojo> xlsExporter = new XLSExporter(SamplePojo.class, false);
        xlsExporter.exportStream(outputStream, TestData.getData().iterator());
        outputStream.flush();
        outputStream.close();
        System.out.println(outputFile.getPath());

        // And with title
        File outputFile2 = FileUtil.getRandomTempFile();
        outputStream = new FileOutputStream(outputFile2);
        XLSExporter<SamplePojo> xlsExporter2 = new XLSExporter(SamplePojo.class, true);
        xlsExporter2.exportStream(outputStream, TestData.getData().iterator());
        outputStream.flush();
        outputStream.close();
        System.out.println(outputFile2.getPath());


        //TODO: Check with imported data

    }
}
