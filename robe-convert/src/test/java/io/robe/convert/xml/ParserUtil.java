package io.robe.convert.xml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;

public class ParserUtil {

    public static JsonParser getParser(String xml) throws Exception {
        XmlFactory factory = new XmlFactory();
        JsonParser parser = factory.createParser(xml);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        return parser;
    }
}
