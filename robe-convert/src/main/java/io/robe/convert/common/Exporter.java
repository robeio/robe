package io.robe.convert.common;

import io.robe.convert.common.annotation.ConvertFieldExport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public abstract class Exporter<T> extends Converter {

    public Exporter(Class dataClass) {
        super(dataClass);
    }

    public abstract void exportStream(OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException;

    protected final boolean isSuitable(ConvertFieldExport ann) {
        return ((ann != null) && !ann.ignore());
    }
}
