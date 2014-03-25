package io.robe.convert.xml;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.robe.convert.IsExporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class XMLExporter extends IsExporter {
    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        HashMap<String,List<T>> hashMap = new HashMap<String, List<T>>();
        hashMap.put(clazz.getSimpleName(),list);
        xmlMapper.writeValue(outputStream,hashMap);
    }
}
