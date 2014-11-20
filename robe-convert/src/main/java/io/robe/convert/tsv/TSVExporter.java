package io.robe.convert.tsv;

import io.robe.convert.csv.CSVExporter;
import org.supercsv.prefs.CsvPreference;

public class TSVExporter<T> extends CSVExporter<T> {

    public TSVExporter(Class dataClass) {
        super(dataClass, CsvPreference.TAB_PREFERENCE);
    }

}
