package io.robe.convert;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public abstract class IsExporter extends Converter {
    public abstract <T> void exportStream(Class clazz, OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException;


}
