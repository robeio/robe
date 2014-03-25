package io.robe.convert.csv;

import io.robe.convert.IsExporter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class CSVExporter extends IsExporter {
    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        if (list == null)
            throw new NullPointerException("List can not be null or empty.");

        Collection<Field> fields = getFields(clazz);
        String[] fieldNames = new String[fields.size()];

        Writer writer = new OutputStreamWriter(outputStream, "UTF-8");

        CellProcessor[] processors = CSVUtil.convertFieldsToCellProcessors(fields, fieldNames);

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        int i = 0;

        for (T entry : list)
            csvBeanWriter.write(entry, fieldNames, processors);
        csvBeanWriter.flush();
        csvBeanWriter.close();
        writer.flush();
        writer.close();

    }
}

