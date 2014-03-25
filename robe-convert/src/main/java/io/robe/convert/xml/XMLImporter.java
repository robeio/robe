package io.robe.convert.xml;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.robe.convert.IsImporter;

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
}
