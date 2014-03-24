package io.robe.convert.importer.excel;

import io.robe.convert.FileEnum;
import io.robe.convert.importer.AbstractImporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class AbstractExcelImporter extends AbstractImporter {
    @Override
    public <T> List<T> importStream(InputStream inputStream, Class pojoClass) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return null;
    }

    public abstract <T> List<T> importExcelStream(FileEnum fileEnum, InputStream inputStream, Class pojoClass) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;
}
