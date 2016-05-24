package io.robe.convert.json;

import io.robe.convert.FileUtil;
import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class JSONExporterTest {


    //@Test
    public void testExportStream() throws Exception {

        // Write to temp file
        File outputFile = FileUtil.getRandomTempFile();
        OutputStream outputStream = new FileOutputStream(outputFile);
        JSONExporter<SamplePojo> exporter = new JSONExporter(SamplePojo.class);
        exporter.exportStream(outputStream, TestData.getData().iterator());
        outputStream.flush();
        outputStream.close();


        JSONImporter<SamplePojo> importer = new JSONImporter(SamplePojo.class);
        List<SamplePojo> list = importer.importStream(new FileInputStream(outputFile.getPath()));
        assert list.size() == TestData.getData().size();
        int index = 0;

        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
            System.out.println(ref);
        }


        outputFile.delete();


    }
}
