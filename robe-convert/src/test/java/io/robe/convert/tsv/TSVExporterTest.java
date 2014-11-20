package io.robe.convert.tsv;

import io.robe.convert.FileUtil;
import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class TSVExporterTest {
    @Test
    public void testExportStream() throws Exception {

        File outputFile = FileUtil.getRandomTempFile();
        OutputStream outputStream = new FileOutputStream(outputFile);
        TSVExporter<SamplePojo> exporter = new TSVExporter(SamplePojo.class);
        exporter.exportStream(outputStream, TestData.getData().iterator());

        TSVImporter<SamplePojo> importer = new TSVImporter<>(SamplePojo.class);
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
