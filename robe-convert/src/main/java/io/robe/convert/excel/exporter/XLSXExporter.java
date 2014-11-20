package io.robe.convert.excel.exporter;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class XLSXExporter<T> extends ExcelExporter<T> {


    public XLSXExporter(Class dataClass) {
        super(dataClass);
    }

    public XLSXExporter(Class dataClass, boolean hasTitleRow) {
        super(dataClass, hasTitleRow);
    }

    @Override
    public void exportStream(OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException {
        Workbook workbook = new XSSFWorkbook();
        exportStream(outputStream, iterator, workbook);
    }
}
