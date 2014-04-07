package io.robe.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class IsImporter extends Converter {
    public abstract <T> List<T> importStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;

    public abstract <T> void importStream(Class clazz, InputStream inputStream, OnItemHandler handler) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;

}
