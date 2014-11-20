package io.robe.convert.tsv;

import io.robe.convert.csv.CSVImporter;
import org.supercsv.prefs.CsvPreference;

public class TSVImporter<T> extends CSVImporter<T> {

    public TSVImporter(Class dataClass) {
        super(dataClass, CsvPreference.TAB_PREFERENCE);
    }

}
