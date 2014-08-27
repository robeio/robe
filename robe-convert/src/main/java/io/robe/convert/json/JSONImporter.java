package io.robe.convert.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.robe.convert.IsImporter;
import io.robe.convert.OnItemHandler;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class JSONImporter extends IsImporter {
    @Override
    public <T> List<T> importStream(Class clazz, InputStream inputStream) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(LinkedList.class, clazz);
        List<T> list = mapper.readValue(inputStream, javaType);
        return list;
    }

    @Override
    public <T> void importStream(Class clazz, InputStream inputStream, OnItemHandler handler) throws Exception {

        JsonFactory factory = new MappingJsonFactory();

        JsonParser parser = factory.createParser(inputStream);

        JsonToken current;

        Collection<Field> fields = getFields(clazz);

        current = parser.nextToken();
        if (current != JsonToken.START_ARRAY) {
            throw new RuntimeException("Error: root should be object or array.");
        }

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentName() == null)
                continue;
            T item = (T) parser.readValueAs(clazz);
            handler.onItem(item);

        }

    }
}
