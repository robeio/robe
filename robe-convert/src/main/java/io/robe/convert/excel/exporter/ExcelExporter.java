package io.robe.convert.excel.exporter;

import io.robe.convert.IsExporter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class ExcelExporter extends IsExporter {
    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(list == null) {
            throw new NullPointerException("List can not be null or empty.");
        }

        Collection<Field> fields = getFields(clazz);
        String[] fieldNames = new String[fields.size()];



    }
}
