package io.robe.convert.xml;

import io.robe.convert.FileUtil;
import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class XMLExporterTest {
    //@Test
    public void testExportStream() throws Exception {

        File outputFile = FileUtil.getRandomTempFile();
        OutputStream outputStream = new FileOutputStream(outputFile);
        XMLExporter<SamplePojo> exporter = new XMLExporter<>(SamplePojo.class);
        exporter.exportStream(outputStream, TestData.getData().iterator());

        XMLImporter<SamplePojo> importer = new XMLImporter<>(SamplePojo.class);
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
