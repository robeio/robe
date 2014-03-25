package io.robe.convert.importer.excel;

import io.robe.convert.IsImporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class IsExcelImporter extends IsImporter {
    public <T> List<T> importStream(InputStream inputStream, Class pojoClass) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return null;
    }
}
