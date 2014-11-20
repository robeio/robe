package io.robe.convert.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.robe.convert.common.Importer;
import io.robe.convert.common.OnItemHandler;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


public class JSONImporter<T> extends Importer<T> {

    public JSONImporter(Class dataClass) {
        super(dataClass);
    }

    @Override
    public List<T> importStream(InputStream inputStream) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(LinkedList.class, getDataClass());
        List<T> list = mapper.readValue(inputStream, javaType);
        return list;
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler) throws Exception {

        JsonFactory factory = new MappingJsonFactory();

        JsonParser parser = factory.createParser(inputStream);

        JsonToken current;

        current = parser.nextToken();
        if (current != JsonToken.START_ARRAY) {
            throw new RuntimeException("Error: root should be object or array.");
        }

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentName() == null)
                continue;
            T item = (T) parser.readValueAs(getDataClass());
            handler.onItem(item);

        }

    }
}
