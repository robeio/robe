package io.robe.convert.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.robe.convert.IsExporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class JSONExporter extends IsExporter {
    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, list);
    }
}
