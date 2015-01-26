package io.robe.convert.common;


import java.io.InputStream;
import java.util.List;

public abstract class Importer<T> extends Converter {
    protected static final String DEFAULT_ENCODING = "UTF-8";

    public Importer(Class dataClass) {
        super(dataClass);
    }

    // Gets all entities at once, ram usage could increase while reading
    public abstract List<T> importStream(InputStream inputStream) throws Exception;

    public abstract List<T> importStream(InputStream inputStream, String charSetName) throws Exception;


    // invokes handler in each atom, good for big sized data sets, less ram usage
    public abstract void importStream(InputStream inputStream, OnItemHandler handler) throws Exception;

    public abstract void importStream(InputStream inputStream, OnItemHandler handler, String charSetName) throws Exception;

    protected class DefaultOnItemHandler implements OnItemHandler<T> {
        private List<T> list = null;

        public DefaultOnItemHandler(List<T> list) {
            this.list = list;
        }

        @Override
        public void onItem(T item) throws Exception {
            list.add(item);
        }
    }
}
