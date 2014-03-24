package io.robe.convert.exporter;

import io.robe.convert.Converter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class AbstractExporter extends Converter {
    public abstract <T> void exportStream(OutputStream inputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;

}
