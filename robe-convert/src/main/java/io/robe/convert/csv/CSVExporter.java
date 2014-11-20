package io.robe.convert.csv;

import io.robe.convert.common.Exporter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

public class CSVExporter<T> extends Exporter<T> {
    private CsvPreference preference = null;
    private Collection<FieldEntry> fields = null;
    private String[] fieldNames = null;
    private CellProcessor[] processors = null;

    public CSVExporter(Class clazz) {
        this(clazz, CsvPreference.EXCEL_PREFERENCE.STANDARD_PREFERENCE);
    }

    public CSVExporter(Class clazz, CsvPreference preference) {
        super(clazz);
        this.preference = preference;
        this.fields = getFields(clazz);
        this.fieldNames = new String[fields.size()];
        this.processors = CSVUtil.convertFieldsToCellProcessors(this.fields, this.fieldNames);
    }

    @Override
    public void exportStream(OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException {
        if (iterator == null)
            throw new NullPointerException("List can not be null or empty.");

        Writer writer = new OutputStreamWriter(outputStream, "UTF-8");

        ICsvBeanWriter beanWriter = new CsvBeanWriter(writer, preference);

        while (iterator.hasNext()) {
            T entry = iterator.next();
            beanWriter.write(entry, fieldNames, processors);
        }
        beanWriter.flush();
    }
}

