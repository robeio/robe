package io.robe.convert.xml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.robe.convert.IsImporter;
import io.robe.convert.OnItemHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class XMLImporter  extends IsImporter{
    @Override
    public <T> List<T> importStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        XmlMapper xmlMapper = new XmlMapper();
        List<T> list ;
        TypeFactory t = TypeFactory.defaultInstance();
        list = xmlMapper.readValue(inputStream, t.constructCollectionType(LinkedList.class,clazz));
        return list;
    }

    @Override
    public <T> void importStream(Class clazz, InputStream inputStream, OnItemHandler handler) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        XmlMapper xmlMapper = new XmlMapper();
        XmlFactory factory = new XmlFactory();

        JsonParser parser = factory.createParser(inputStream);

        TypeFactory t = TypeFactory.defaultInstance();
//        MappingIterator<T> iterator = xmlMapper.readValues(parser, t.constructCollectionType(LinkedList.class,clazz));
        JsonToken current;
//        while (iterator.hasNext()){
//            iterator.nextValue();
//        }

//        Collection<Field> fields = getFields(clazz);
//
        current = parser.nextToken();
        if (current != JsonToken.START_OBJECT) {
            throw new RuntimeException("Error: root should be object or array.");
        }
        parser.setCodec(xmlMapper);

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            if (parser.getCurrentName() == null)
                continue;
            T item = (T) xmlMapper.readValue(parser, t.constructType(clazz));
            System.out.println(parser.getValueAsString());
            handler.onItem(item);

        }

    }
}
