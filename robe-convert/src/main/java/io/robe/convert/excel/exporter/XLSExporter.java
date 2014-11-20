package io.robe.convert.excel.exporter;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class XLSExporter<T> extends ExcelExporter<T> {
    boolean isFirstRowHeader = false;

    public XLSExporter(Class dataClass) {
        this(dataClass, false);
    }

    public XLSExporter(Class dataClass, boolean hasTitleRow) {
        super(dataClass, hasTitleRow);
    }


    @Override
    public void exportStream(OutputStream outputStream, Iterator<T> iterator) throws IOException, ClassNotFoundException, IllegalAccessException {
        Workbook workbook = new HSSFWorkbook();
        exportStream(outputStream, iterator, workbook);
    }
}
