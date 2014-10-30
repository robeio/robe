package io.robe.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class Converter {

    /*
     * Paramterized method to sort Map e.g. HashMap or Hashtable in Java
     * throw NullPointerException if Map contains null key
     */
    public final static <K extends Comparable, V> Map<K, V> sortByKeys(Map<K, V> map) {
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (K key : keys) {
            sortedMap.put(key, map.get(key));
        }
        return sortedMap;
    }

    /**
     * Returns an ordered list of the fields which belongs to the given class.
     *
     * @param clazz class to get types.
     * @return ordered list of fields.
     */
    protected final Collection<Field> getFields(Class clazz) {
        Map<Field, Integer> fieldOrder = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            Annotation fieldAnnotation = field.getAnnotation(MappingProperty.class);
            MappingProperty fieldMappingProperties = (MappingProperty) fieldAnnotation;
            if (fieldMappingProperties != null) {
                if (!fieldMappingProperties.hidden()) {
                    fieldOrder.put(field, fieldMappingProperties.order());
                }
            }
        }
        Object[] toArray = fieldOrder.entrySet().toArray();
        Arrays.sort(toArray, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<Field, Integer>) o1).getValue().compareTo(
                        ((Map.Entry<Field, Integer>) o2).getValue());
            }
        });

        Collection<Field> fields = new ArrayList<>();
        for (Object array : toArray) {
            Map.Entry<Field, Integer> entry = (Map.Entry<Field, Integer>) array;
            fields.add(entry.getKey());
        }
        return fields;
    }

    /**
     * Returns a map of the fields which belongs to the given class with field name as key.
     *
     * @param clazz class to get types.
     * @return ordered list of fields.
     */
    protected final Map<String, Field> getFieldMap(Class clazz) {
        Map<String, Field> fieldList = new HashMap<String, Field>();
        for (Field field : clazz.getDeclaredFields()) {
            Annotation fieldAnnotation = field.getAnnotation(MappingProperty.class);
            MappingProperty fieldMappingProperties = (MappingProperty) fieldAnnotation;
            if (fieldMappingProperties != null) {
                fieldList.put(field.getName(), field);
            }
        }

        return fieldList;

    }

}
