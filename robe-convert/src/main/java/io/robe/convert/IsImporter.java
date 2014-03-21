package io.robe.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IsImporter {
    public <T> List<T> importStream(InputStream inputStream, Class pojoClass) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;
}
