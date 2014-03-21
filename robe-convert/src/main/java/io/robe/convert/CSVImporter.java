package io.robe.convert;

import io.robe.convert.pojo.CSVPojo;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CSVImporter implements IsImporter {
    @Override
    public <T> List<T> importStream(InputStream inputStream, Class pojoClass) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Field[] fieldList = new Field[pojoClass.getDeclaredFields().length + 1];

        String[] header = new String[pojoClass.getFields().length];

        CellProcessor[] cellProcessors = new CellProcessor[pojoClass.getFields().length];
        int i = 0;
        for (Field field : pojoClass.getFields()) {
            Annotation fieldAnnotation = field.getAnnotation(MappingProperty.class);
            MappingProperty fieldMappingProperties = (MappingProperty) fieldAnnotation;
            header[i] = field.getName();
            i++;
            if (fieldMappingProperties != null) {
                fieldList[fieldMappingProperties.order()] = field;
            }
        }

        Reader reader = new InputStreamReader(inputStream);
        ICsvBeanReader csvBeanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);

        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(new ParseInt()),
                new Optional(),
                new Optional()
        };

        List<T> list = new ArrayList<T>();
        Object obj;
        while ((obj = csvBeanReader.read(CSVPojo.class, header, processors)) != null) { //new String[]{"id", "nameSurname", "job"}
            list.add((T) obj);
        }
        return (List) list;
    }
}
