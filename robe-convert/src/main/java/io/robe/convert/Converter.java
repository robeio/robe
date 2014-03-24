package io.robe.convert;

import org.supercsv.cellprocessor.ift.CellProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class Converter {

    protected final Collection<Field> getFields(Class clazz) {

        Map<Integer, Field> fieldList = new HashMap<Integer, Field>();

        String[] fieldNames = new String[clazz.getFields().length];

        CellProcessor[] cellProcessors = new CellProcessor[clazz.getFields().length];
        for (Field field : clazz.getDeclaredFields()) {
            Annotation fieldAnnotation = field.getAnnotation(MappingProperty.class);
            MappingProperty fieldMappingProperties = (MappingProperty) fieldAnnotation;
            if (fieldMappingProperties != null) {
                fieldList.put(fieldMappingProperties.order(), field);
            }
        }
        fieldList = sortByKeys(fieldList);

        return fieldList.values();


    }


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

}
