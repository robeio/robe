package io.robe.convert.xml.parsers;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;


public interface IsParser<T> {

    T parse(JsonParser parser, Field field) throws IOException;

    default boolean isValid(JsonParser o) throws IOException {
        return o.getValueAsString() != null && !o.getValueAsString().trim().isEmpty();
    }
}
