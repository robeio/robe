package io.robe.convert.xml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import io.robe.convert.common.Importer;
import io.robe.convert.common.OnItemHandler;
import io.robe.convert.xml.parsers.Parsers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class XMLImporter<T> extends Importer<T> {


    public XMLImporter(Class dataClass) {
        super(dataClass);
    }

    @Override
    public List<T> importStream(InputStream inputStream) throws Exception {

        final List<T> list = new LinkedList<T>();
        OnItemHandler<T> handler = new OnItemHandler<T>() {
            @Override
            public void onItem(T item) {
                list.add(item);
            }
        };
        return list;
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler) throws Exception {
        XmlFactory factory = new XmlFactory();
        JsonParser parser = factory.createParser(inputStream);
        JsonToken current;

        Map<String, Field> fields = getFieldMap(getDataClass());

        current = parser.nextToken();
        while (current != JsonToken.START_OBJECT) {
            throw new RuntimeException("Error: root should be object.");

        }

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            try {
                parser.getCurrentName();
            } catch (Exception e) {
                continue;
            }
            if (getDataClass().getSimpleName().equals(parser.getValueAsString())) {
                T item = (T) getDataClass().newInstance();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    if (parser.getValueAsString() == null || parser.getCurrentToken() == JsonToken.FIELD_NAME)
                        continue;
                    Field field = fields.get(parser.getCurrentName());
                    setField(parser, item, field);
                }
                handler.onItem(item);
            }

        }

    }

    private <T> void setField(JsonParser parser, T item, Field field) throws IllegalAccessException, IOException {
        boolean acc = field.isAccessible();
        field.setAccessible(true);
        field.set(item, Parsers.valueOf(field.getType().getSimpleName().toUpperCase(Locale.ENGLISH)).getParser().parse(parser, field));
        field.setAccessible(acc);
    }


}
