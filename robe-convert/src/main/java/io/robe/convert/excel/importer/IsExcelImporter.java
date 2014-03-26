package io.robe.convert.excel.importer;

import io.robe.convert.IsImporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class IsExcelImporter extends IsImporter {
    public <T> List<T> importStream(InputStream inputStream, Class pojoClass) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return null;
    }

    public abstract <T> List<T> importXSLStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;

    public abstract <T> List<T> importXSLXStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;
}
