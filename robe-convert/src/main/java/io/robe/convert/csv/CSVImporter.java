package io.robe.convert.csv;

import io.robe.convert.IsImporter;
import io.robe.convert.OnItemHandler;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVImporter extends IsImporter {
    CsvPreference preference = CsvPreference.EXCEL_PREFERENCE.STANDARD_PREFERENCE;

    public CSVImporter() {
    }

    public CSVImporter(CsvPreference preference) {
        this.preference = preference;
    }

    @Override
    public <T> List<T> importStream(Class clazz, InputStream inputStream) throws Exception {

        final List<T> list = new ArrayList<T>();

        OnItemHandler<T> handler = new OnItemHandler<T>() {
            @Override
            public void onItem(T item) {
                list.add(item);
            }
        };
        try {
            this.<T>importStream(clazz, inputStream, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public <T> void importStream(Class clazz, InputStream inputStream, OnItemHandler handler) throws Exception {

        Collection<Field> fields = getFields(clazz);
        String[] fieldNames = new String[fields.size()];

        Reader reader = new InputStreamReader(inputStream);

        CellProcessor[] processors = CSVUtil.convertFieldsToCellProcessors(fields, fieldNames);

        ICsvBeanReader csvBeanReader = new CsvBeanReader(reader, preference);
        Object obj;
        while ((obj = csvBeanReader.read(clazz, fieldNames, processors)) != null) {
            handler.onItem((T) obj);
        }
    }

}
