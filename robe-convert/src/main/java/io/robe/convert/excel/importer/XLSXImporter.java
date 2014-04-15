package io.robe.convert.excel.importer;

import io.robe.convert.OnItemHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XLSXImporter extends ExcelImporter {

    boolean isFirstRowHeader = false;

    public XLSXImporter(boolean isFirstRowHeader) {
        this.isFirstRowHeader = isFirstRowHeader;
    }

    @Override
    public <T> List<T> importStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return importStream(clazz, inputStream, isFirstRowHeader, new XSSFWorkbook(inputStream));
    }

    @Override
    public <T> void importStream(Class clazz, InputStream inputStream, OnItemHandler handler) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        importStream(clazz, inputStream, isFirstRowHeader, new XSSFWorkbook(inputStream), handler);
    }
}
