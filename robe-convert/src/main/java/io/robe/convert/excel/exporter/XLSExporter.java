package io.robe.convert.excel.exporter;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class XLSExporter extends ExcelExporter {
    boolean isFirstRowHeader = false;

    public XLSExporter(boolean isFirstRowHeader) {
        this.isFirstRowHeader = isFirstRowHeader;
    }

    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException {
        Workbook workbook = new HSSFWorkbook();
        exportStream(clazz, outputStream, iterator, isFirstRowHeader, workbook);
    }
}
