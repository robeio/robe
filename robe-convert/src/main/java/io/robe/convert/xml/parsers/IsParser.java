package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;


public interface IsParser<T> {

    public T parse(JsonParser parser, Field field) throws IOException;
}
