package io.robe.convert.common;

import io.robe.convert.SamplePojo;
import io.robe.convert.common.annotation.Convert;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class ConverterTest {

    private class TestConverter extends Converter {

        public TestConverter(Class dataClass) {
            super(dataClass);
        }
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

    }

    @Test
    public void getFieldMap() throws Exception {

    }

}