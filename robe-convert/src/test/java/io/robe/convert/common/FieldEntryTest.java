package io.robe.convert.common;

import io.robe.convert.SamplePojo;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class FieldEntryTest {
    @Test
    public void getKey() throws Exception {
        Converter.FieldEntry entry = new Converter.FieldEntry(0, SamplePojo.class.getDeclaredField("name"));
        assertEquals(Integer.valueOf(0), entry.getKey());
    }

    @Test
    public void getValue() throws Exception {
        Field field1 = SamplePojo.class.getDeclaredField("name");
        Field field2 = SamplePojo.class.getDeclaredField("surname");
        Converter.FieldEntry entry = new Converter.FieldEntry(0, field1);
        assertEquals(field1, entry.getValue());
        entry.setValue(field2);
        assertEquals(field2, entry.getValue());
    }


    @Test
    public void compareTo() throws Exception {
        Field field1 = SamplePojo.class.getDeclaredField("name");
        Field field2 = SamplePojo.class.getDeclaredField("surname");
        Converter.FieldEntry entry1 = new Converter.FieldEntry(0, field1);
        Converter.FieldEntry entry2 = new Converter.FieldEntry(1, field2);
        assertTrue(entry1.compareTo(entry2) < 0);
        assertTrue(entry2.compareTo(entry1) > 0);
    }

}