package io.robe.convert.xml;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import io.robe.convert.common.Exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class XMLExporter<T> extends Exporter<T> {

    public XMLExporter(Class dataClass) {
        super(dataClass);
    }

    @Override
    public void exportStream(OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        XmlFactory factory = new XmlFactory();
        ToXmlGenerator generator = factory.createGenerator(outputStream);


        generator.setCodec(xmlMapper);
        generator.writeRaw("<xml>");

        while (iterator.hasNext()) {

            generator.writeRaw(xmlMapper.writeValueAsString(iterator.next()));
        }
        generator.writeRaw("</xml>");

        generator.flush();
    }
}
