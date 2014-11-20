package io.robe.convert.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.robe.convert.common.Exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;


public class JSONExporter<T> extends Exporter<T> {

    public JSONExporter(Class dataClass) {
        super(dataClass);
    }

    @Override
    public void exportStream(OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, iterator);
    }
}
