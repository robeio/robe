package io.robe.convert.excel.exporter;

import io.robe.convert.IsExporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class IsExcelExporter extends IsExporter {

    public abstract <T> void exportXSLStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;

    public abstract <T> void exportXSLXStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;
}
