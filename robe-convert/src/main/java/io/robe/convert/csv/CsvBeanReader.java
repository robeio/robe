package io.robe.convert.csv;

/**
 * Created by hasanmumin on 20/10/2016.
 */

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.AbstractCsvReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ITokenizer;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.BeanInterfaceProxy;
import org.supercsv.util.MethodCache;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CsvBeanReader extends AbstractCsvReader implements ICsvBeanReader {
    private final List<Object> processedColumns = new ArrayList();
    private final MethodCache cache = new MethodCache();

    public CsvBeanReader(Reader reader, CsvPreference preferences) {
        super(reader, preferences);
    }

    public CsvBeanReader(ITokenizer tokenizer, CsvPreference preferences) {
        super(tokenizer, preferences);
    }

    private static <T> T instantiateBean(Class<T> clazz) {
        Object bean;
        if (clazz.isInterface()) {
            bean = BeanInterfaceProxy.createProxy(clazz);
        } else {
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException var3) {
                throw new SuperCsvReflectionException(String.format("error instantiating bean, check that %s has a default no-args constructor", new Object[]{clazz.getName()}), var3);
            } catch (IllegalAccessException var4) {
                throw new SuperCsvReflectionException("error instantiating bean", var4);
            }
        }

        return (T) bean;
    }

    private static void invokeSetter(Object bean, Method setMethod, Object fieldValue) {
        try {
            setMethod.invoke(bean, new Object[]{fieldValue});
        } catch (Exception var4) {
            throw new SuperCsvReflectionException(String.format("error invoking method %s()", new Object[]{setMethod.getName()}), var4);
        }
    }

    private <T> T populateBean(T resultBean, String[] nameMapping) {
        for (int i = 0; i < nameMapping.length; ++i) {
            Object fieldValue = this.processedColumns.get(i);
            if (nameMapping[i] != null && fieldValue != null) {
                Method setMethod = this.cache.getSetMethod(resultBean, nameMapping[i], fieldValue.getClass());
                invokeSetter(resultBean, setMethod, fieldValue);
            }
        }

        return resultBean;
    }

    public <T> T read(Class<T> clazz, String... nameMapping) throws IOException {
        if (clazz == null) {
            throw new NullPointerException("clazz should not be null");
        } else if (nameMapping == null) {
            throw new NullPointerException("nameMapping should not be null");
        } else {
            return this.readIntoBean(instantiateBean(clazz), nameMapping, (CellProcessor[]) null);
        }
    }

    public <T> T read(Class<T> clazz, String[] nameMapping, CellProcessor... processors) throws IOException {
        if (clazz == null) {
            throw new NullPointerException("clazz should not be null");
        } else if (nameMapping == null) {
            throw new NullPointerException("nameMapping should not be null");
        } else if (processors == null) {
            throw new NullPointerException("processors should not be null");
        } else {
            return this.readIntoBean(instantiateBean(clazz), nameMapping, processors);
        }
    }

    public <T> T read(T bean, String... nameMapping) throws IOException {
        if (bean == null) {
            throw new NullPointerException("bean should not be null");
        } else if (nameMapping == null) {
            throw new NullPointerException("nameMapping should not be null");
        } else {
            return this.readIntoBean(bean, nameMapping, (CellProcessor[]) null);
        }
    }

    public <T> T read(T bean, String[] nameMapping, CellProcessor... processors) throws IOException {
        if (bean == null) {
            throw new NullPointerException("bean should not be null");
        } else if (nameMapping == null) {
            throw new NullPointerException("nameMapping should not be null");
        } else if (processors == null) {
            throw new NullPointerException("processors should not be null");
        } else {
            return this.readIntoBean(bean, nameMapping, processors);
        }
    }

    private <T> T readIntoBean(T bean, String[] nameMapping, CellProcessor[] processors) throws IOException {
        if (this.readRow()) {
            if (nameMapping.length != this.length()) {

                if (nameMapping.length < this.length()) {
                    throw new IllegalArgumentException(String.format("the nameMapping array and the number of columns read should be the same size (nameMapping length = %d, columns = %d)", new Object[]{Integer.valueOf(nameMapping.length), Integer.valueOf(this.length())}));
                }

                while (nameMapping.length > this.length()) {
                    this.getColumns().add("");
                }
            }
            if (processors == null) {
                this.processedColumns.clear();
                this.processedColumns.addAll(this.getColumns());
            } else {
                this.executeProcessors(this.processedColumns, processors);
            }

            return this.populateBean(bean, nameMapping);

        } else {
            return null;
        }
    }
}
