package io.robe.convert.excel.importer;

import io.robe.convert.OnItemHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XLSImporter extends ExcelImporter {
    private boolean hasTitleRow = false;

    public XLSImporter(boolean hasTitleRow) {
        this.hasTitleRow = hasTitleRow;
    }

    @Override
    public <T> List<T> importStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return importStream(clazz, inputStream, hasTitleRow, new HSSFWorkbook(inputStream));
    }

    @Override
    public <T> void importStream(Class clazz, InputStream inputStream, OnItemHandler handler) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        importStream(clazz, inputStream, hasTitleRow, new HSSFWorkbook(inputStream), handler);
    }

    public boolean hasTitleRow() {
        return hasTitleRow;
    }

    public void setHasTitleRow(boolean hasTitleRow) {
        this.hasTitleRow = hasTitleRow;
    }
}
