package io.robe.convert.csv;

import io.robe.convert.SamplePojo;
import io.robe.convert.common.Converter;
import io.robe.convert.common.annotation.Convert;
import org.junit.Test;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class CSVUtilTest {

    @Test
    public void convertFieldsToCellProcessors() throws Exception {
        Collection<Converter.FieldEntry> entries = new LinkedList<>();
        Field[] fields = SamplePojo.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Convert convert = fields[i].getDeclaredAnnotation(Convert.class);
            if (convert != null && !convert.ignore()) {
                entries.add(new Converter.FieldEntry(i, fields[i]));
            }
        }
        String[] fieldNames = new String[entries.size()];

        CellProcessor[] processors = CSVUtil.convertFieldsToCellProcessors(entries, fieldNames);

        assertEquals(10, fieldNames.length);
        assertEquals(10, processors.length);

    }

    @Test
    public void decideAdaptor() throws Exception {
        Field[] fields = SamplePojo.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            CellProcessorAdaptor adapter = CSVUtil.decideAdaptor(fields[i]);
            if(!fields[i].getType().getTypeName().equals(String.class.getName())){
                assertNotNull(adapter);
            }
        }
    }

}