package io.robe.convert.common;

import io.robe.convert.SamplePojo;
import io.robe.convert.common.annotation.Convert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

public class ConverterTest {

    private class TestConverter extends Converter {

        public TestConverter(Class dataClass) {
            super(dataClass);
        }
    }

    private  class InnerSamplePojo extends SamplePojo{
        @Convert
        private String innerName;
    }

    @Test
    public void getDataClass() throws Exception {
        Converter converter = new TestConverter(SamplePojo.class);
        assertEquals(SamplePojo.class, converter.getDataClass());
    }

    @Test
    public void isSuitable() throws Exception {
        Converter converter = new TestConverter(SamplePojo.class);
        assertFalse(converter.isSuitable(null));
        assertFalse(converter.isSuitable(SamplePojo.class.getDeclaredField("ignoreString").getDeclaredAnnotation(Convert.class)));
        assertTrue(converter.isSuitable(SamplePojo.class.getDeclaredField("name").getDeclaredAnnotation(Convert.class)));

    }

    @Test
    public void getFields() throws Exception {
        Converter converter = new TestConverter(SamplePojo.class);
        Collection<Converter.FieldEntry> fields = converter.getFields(SamplePojo.class);
        assertEquals(10, fields.size()); // will not take ignored fields.
    }

    @Test
    public void getAllFields() throws Exception {
        Converter converter = new TestConverter(SamplePojo.class);
        Collection<Converter.FieldEntry> fields = converter.getAllFields(InnerSamplePojo.class);
        assertEquals(11, fields.size()); // will not take ignored fields.
    }

    @Test
    public void getFieldMap() throws Exception {
        Converter converter = new TestConverter(SamplePojo.class);
        Map<String,Field> fields = converter.getFieldMap(InnerSamplePojo.class);
        assertEquals(11, fields.size()); // will not take ignored fields.
    }

}