package io.robe.convert;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class IsExporter extends Converter {
    public abstract <T> void exportStream(Class clazz,OutputStream inputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;

}
