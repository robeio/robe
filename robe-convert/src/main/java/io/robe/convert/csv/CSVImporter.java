package io.robe.convert.csv;

import io.robe.convert.AbstractImporter;
import io.robe.convert.MappingProperty;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CSVImporter extends AbstractImporter {
    @Override
    public <T> List<T> importStream(InputStream inputStream, Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {


        Collection<Field> fields = getFields(clazz);
        String[] fieldNames = new String[fields.size()];

        Reader reader = new InputStreamReader(inputStream);

        CellProcessor[] processors = convertFieldsToCellProcessors(fields, fieldNames);

        List<T> list = new ArrayList<T>();
        ICsvBeanReader csvBeanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
        Object obj;
        while ((obj = csvBeanReader.read(clazz, fieldNames, processors)) != null) {
            list.add((T) obj);
        }
        return list;
    }

    private CellProcessor[] convertFieldsToCellProcessors(Collection<Field> fields, String[] fieldNames) {
        CellProcessor[] processors = new CellProcessor[fields.size()];
        int i = 0;
        for (Field field : fields) {
            MappingProperty an = field.getAnnotation(MappingProperty.class);

            CellProcessorAdaptor a = decideAdaptor(field);
            CellProcessor p = null;
            if (an.optional()) {
                if (a != null) {
                    p = new Optional(a);
                } else {
                    p = new Optional();
                }
            } else {
                if (a != null) {
                    p = new NotNull(a);
                } else {
                    p = new NotNull();
                }
            }
            fieldNames[i] = field.getName();
            processors[i++] = p;
        }
        return processors;
    }

    private CellProcessorAdaptor decideAdaptor(Field field) {
        String fieldType = field.getType().toString();
        if (fieldType.equals("int")) {
            return new ParseInt();
        } else if (fieldType.equals("long")) {
            return new ParseLong();
        } else if (fieldType.equals("double")) {
            return new ParseDouble();
        } else if (fieldType.equals(BigDecimal.class.toString())) {
            return new ParseBigDecimal();
        } else if (fieldType.equals(Date.class.toString())) {
            return new ParseDate("");
        } else if (fieldType.equals("char")) {
            return new ParseChar();
        } else {
            return null;
        }
    }
}
