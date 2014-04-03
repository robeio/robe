package io.robe.convert.tsv;

import io.robe.convert.csv.CSVExporter;
import org.supercsv.prefs.CsvPreference;

public class TSVExporter extends CSVExporter {
    public TSVExporter() {
        super(CsvPreference.TAB_PREFERENCE);
    }
}
