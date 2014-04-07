package io.robe.convert.xml;

import io.robe.convert.OnItemHandler;
import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.util.List;

public class XMLImporterTest {
    @Test
    public void testImportStream() throws Exception {
        XMLImporter xmlImporter = new XMLImporter();

        List<SamplePojo> list = xmlImporter.<SamplePojo>importStream( SamplePojo.class,XMLImporterTest.class.getClassLoader().getResourceAsStream("sample.xml"));

        for(SamplePojo pojo: list){
            System.out.println(pojo);
        }

    }

    @Test
    public void testImportStreamByItem() throws Exception {
        XMLImporter importer = new XMLImporter();
        OnItemHandler<SamplePojo> handler = new OnItemHandler<SamplePojo>() {
            @Override
            public void onItem(SamplePojo samplePojo) {
                System.out.println(samplePojo.toString());
            }
        };
        importer.<SamplePojo>importStream(SamplePojo.class, XMLImporterTest.class.getClassLoader().getResourceAsStream("sample.xml"), handler);

    }
}
