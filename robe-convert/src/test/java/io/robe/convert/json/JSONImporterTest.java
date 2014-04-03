package io.robe.convert.json;

import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.util.List;

/**
 * Created by serayuzgur on 03/04/14.
 */
public class JSONImporterTest {
    @Test
    public void testImportStream() throws Exception {


        JSONImporter importer = new JSONImporter();
        List<SamplePojo> list = importer.<SamplePojo>importStream(SamplePojo.class, JSONImporterTest.class.getClassLoader().getResourceAsStream("sample.json"));

        for (SamplePojo pojo : list) {
            System.out.println(pojo.toString());
        }

    }
}
