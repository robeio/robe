package io.robe.convert.xml;

import io.robe.convert.SamplePojo;
import io.robe.convert.TestData;
import io.robe.convert.common.OnItemHandler;
import org.junit.Test;

import java.util.List;

public class XMLImporterTest {
    @Test
    public void testImportStream() throws Exception {
        XMLImporter<SamplePojo> xmlImporter = new XMLImporter<>(SamplePojo.class);

        List<SamplePojo> list = xmlImporter.importStream(XMLImporterTest.class.getClassLoader().getResourceAsStream("sample.xml"));
        assert list.size() == TestData.getData().size();

        int index = 0;
        for (SamplePojo item : list) {
            SamplePojo ref = TestData.getData().get(index++);
            assert item.equals(ref);
            System.out.println(ref);
        }

    }

    @Test
    public void testImportStreamByItem() throws Exception {
        XMLImporter<SamplePojo> importer = new XMLImporter<>(SamplePojo.class);
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo samplePojo) {
                System.out.println(samplePojo.toString());
            }
        };
        importer.importStream(XMLImporterTest.class.getClassLoader().getResourceAsStream("sample.xml"), handler);

    }
}
