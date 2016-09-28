package io.robe.convert.excel.importer;

import io.robe.convert.common.OnItemHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

public class XLSImporter<T> extends ExcelImporter<T> {

    private static Logger LOGGER = LoggerFactory.getLogger(XLSImporter.class);

    public XLSImporter(Class dataClass) {
        super(dataClass);
    }

    public XLSImporter(Class dataClass, boolean hasTitleRow) {
        super(dataClass, hasTitleRow);
    }

    @Override
    public List<T> importStream(InputStream inputStream) throws Exception {
        return importStream(new HSSFWorkbook(inputStream));
    }

    @Override
    public List<T> importStream(InputStream inputStream, String charSetName) throws Exception {
        LOGGER.warn("Charset" + charSetName + " ignored, Apache poi detects encoding dynamically");
        return importStream(inputStream);
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler) throws Exception {
        importStream(new HSSFWorkbook(inputStream), handler);
    }

    @Override
    public void importStream(InputStream inputStream, OnItemHandler handler, String charSetName) throws Exception {
        LOGGER.warn("Charset" + charSetName + " ignored, Apache poi detects encoding dynamically");
        importStream(new HSSFWorkbook(inputStream), handler);
    }

}
