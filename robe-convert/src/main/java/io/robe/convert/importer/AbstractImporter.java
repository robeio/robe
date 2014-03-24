package io.robe.convert.importer;

import io.robe.convert.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class AbstractImporter extends Converter {
    public abstract <T> List<T> importStream(InputStream inputStream, Class pojoClass) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;
}
