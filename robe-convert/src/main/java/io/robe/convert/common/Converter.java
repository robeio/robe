package io.robe.convert.common;

import io.robe.convert.common.annotation.Convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Abstract Converter class to include common concepts and util methods
 */
public abstract class Converter {

    /**
     * Holds the class of data type.
     */
    private final Class dataClass;

    public Converter(Class dataClass) {
        this.dataClass = dataClass;
    }


    /**
     * Returns the class of data type  which was given at constructor.
     *
     * @return data class.
     */
    public Class getDataClass() {
        return dataClass;
    }


    protected final boolean isSuitable(Convert ann) {
        return ((ann != null) && !ann.ignore());
    }

    /**
     * Returns an ordered list of the fields which belongs to the given class.
     *
     * @param clazz class to get types.
     * @return ordered list of fields.
     */
    protected final Collection<FieldEntry> getFields(Class clazz) {
        LinkedList<FieldEntry> fieldList = getAllFields(clazz);

        Collections.sort(fieldList, new Comparator<FieldEntry>() {
            public int compare(FieldEntry o1, FieldEntry o2) {
                return o1.compareTo(o2);
            }
        });
        return fieldList;
    }

    protected final LinkedList<FieldEntry> getAllFields(Class clazz) {
        LinkedList<FieldEntry> fieldList = new LinkedList<>();

        if (clazz.getSuperclass() != null) {
            fieldList = getAllFields(clazz.getSuperclass());
        }
        for (Field field : clazz.getDeclaredFields()) {
            Convert cfAnn = field.getAnnotation(Convert.class);
            if (isSuitable(cfAnn)) {
                fieldList.add(new FieldEntry(cfAnn.order(), field));
            }
        }

        return fieldList;
    }


    /**
     * Returns a map of the fields which belongs to the given class with field name as key.
     *
     * @param clazz class to get types.
     * @return ordered list of fields.
     */
    protected final Map<String, Field> getFieldMap(Class clazz) {
        Map<String, Field> fieldList = new HashMap<String, Field>();
        for (FieldEntry entry : getAllFields(clazz)) {
            Field field = entry.getValue();
            fieldList.put(field.getName(), field);
        }
        return fieldList;

    }


    public static class FieldEntry implements Map.Entry<Integer, Field>, Comparable<FieldEntry> {
        private Integer key = null;
        private Field value = null;

        public FieldEntry(Integer key, Field value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return this.key;
        }

        @Override
        public Field getValue() {
            return this.value;
        }

        @Override
        public Field setValue(Field value) {
            this.value = value;
            return value;
        }

        @Override
        public int compareTo(FieldEntry o) {
            return this.getKey().compareTo(o.getKey());
        }
    }

}
