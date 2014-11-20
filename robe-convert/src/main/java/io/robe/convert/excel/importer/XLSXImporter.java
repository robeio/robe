package io.robe.convert.excel.importer;

import io.robe.convert.common.OnItemHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.List;

public class XLSXImporter<T> extends ExcelImporter<T> {

    public XLSXImporter(Class dataClass) {
        super(dataClass);
    }

    public XLSXImporter(Class dataClass, boolean hasTitleRow) {
        super(dataClass, hasTitleRow);
    }

    @Override
    public List<T> importStream(InputStream inputStream) throws Exception {
        return importStream(inputStream, new XSSFWorkbook(inputStream));
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler) throws Exception {
        importStream(inputStream, new XSSFWorkbook(inputStream), handler);
    }
}
