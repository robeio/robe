package io.robe.convert.excel.importer;

import io.robe.convert.OnItemHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.List;

public class XLSXImporter extends ExcelImporter {

    private boolean hasTitleRow = false;

    public XLSXImporter(boolean hasTitleRow) {
        this.hasTitleRow = hasTitleRow;
    }

    @Override
    public <T> List<T> importStream(Class clazz, InputStream inputStream) throws Exception {
        return importStream(clazz, inputStream, hasTitleRow(), new XSSFWorkbook(inputStream));
    }

    @Override
    public <T> void importStream(Class clazz, InputStream inputStream, OnItemHandler handler) throws Exception {
        importStream(clazz, inputStream, hasTitleRow(), new XSSFWorkbook(inputStream), handler);
    }

    public boolean hasTitleRow() {
        return hasTitleRow;
    }

    public void setHasTitleRow(boolean hasTitleRow) {
        this.hasTitleRow = hasTitleRow;
    }
}
