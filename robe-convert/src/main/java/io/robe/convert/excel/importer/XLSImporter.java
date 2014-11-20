package io.robe.convert.excel.importer;

import io.robe.convert.common.OnItemHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.InputStream;
import java.util.List;

public class XLSImporter<T> extends ExcelImporter<T> {


    public XLSImporter(Class dataClass) {
        super(dataClass);
    }

    public XLSImporter(Class dataClass, boolean hasTitleRow) {
        super(dataClass, hasTitleRow);
    }

    @Override
    public List<T> importStream(InputStream inputStream) throws Exception {
        return importStream(inputStream, new HSSFWorkbook(inputStream));
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler) throws Exception {
        importStream(inputStream, new HSSFWorkbook(inputStream), handler);
    }

}
