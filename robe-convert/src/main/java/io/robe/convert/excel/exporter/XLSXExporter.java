package io.robe.convert.excel.exporter;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class XLSXExporter extends ExcelExporter {

    private final boolean isFirstRowHeader;

    public XLSXExporter(boolean isFirstRowHeader) {
        this.isFirstRowHeader = isFirstRowHeader;
    }


    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException {
        Workbook workbook = new XSSFWorkbook();
        exportStream(clazz, outputStream, iterator, isFirstRowHeader, workbook);
    }
}
