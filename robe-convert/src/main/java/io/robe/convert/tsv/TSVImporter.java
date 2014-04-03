package io.robe.convert.tsv;

import io.robe.convert.csv.CSVImporter;
import org.supercsv.prefs.CsvPreference;

public class TSVImporter extends CSVImporter {

    public TSVImporter() {
        super(CsvPreference.TAB_PREFERENCE);
    }
}
