package io.robe.convert.csv;

import io.robe.convert.IsImporter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVImporter extends IsImporter {
    @Override
    public <T> List<T> importStream(Class clazz,InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        Collection<Field> fields = getFields(clazz);
        String[] fieldNames = new String[fields.size()];

        Reader reader = new InputStreamReader(inputStream);

        CellProcessor[] processors = CSVUtil.convertFieldsToCellProcessors(fields, fieldNames);

        List<T> list = new ArrayList<T>();
        ICsvBeanReader csvBeanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);
        Object obj;
        while ((obj = csvBeanReader.read(clazz, fieldNames, processors)) != null) {
            list.add((T) obj);
        }
        return list;
    }

}
