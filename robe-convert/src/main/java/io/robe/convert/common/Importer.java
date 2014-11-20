package io.robe.convert.common;


import java.io.InputStream;
import java.util.List;

public abstract class Importer<T> extends Converter {
    public Importer(Class dataClass) {
        super(dataClass);
    }

    public abstract List<T> importStream(InputStream inputStream) throws Exception;

    public abstract void importStream(InputStream inputStream, OnItemHandler handler) throws Exception;

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
