package io.robe.convert.xml;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import io.robe.convert.IsExporter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class XMLExporter extends IsExporter {
    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        XmlFactory factory = new XmlFactory();
        ToXmlGenerator generator = factory.createGenerator(outputStream);
        JsonToken current;

        Map<String, Field> fields = getFieldMap(clazz);

        generator.setCodec(xmlMapper);
        generator.writeRaw("<xml>");

        while (iterator.hasNext()) {

            generator.writeRaw(xmlMapper.writeValueAsString(iterator.next()));
        }
        generator.writeRaw("</xml>");

        generator.flush();
    }
}
