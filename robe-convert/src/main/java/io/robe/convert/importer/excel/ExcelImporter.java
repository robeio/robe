package io.robe.convert.importer.excel;

import io.robe.convert.importer.excel.parsers.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ExcelImporter extends IsExcelImporter {

    @Override
    public <T> List<T> importXSLStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Collection<Field> fields = getFields(clazz);

        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<T> entries = new LinkedList<T>();

        // Parserların hazır olsun, rowları dolaşırken kolon indexine göre aynı indexteki parserın parse methodunu çağır döndüğü değeri set et.

        while (rowIterator.hasNext()) {
            T entry = (T) clazz.newInstance();
            fields.iterator().next();
            Row row = rowIterator.next();

            int cellCount = 0;
            for (Field field : fields) {
                Cell cell = row.getCell(cellCount++);
                try {
                    if (cell != null || cell.toString().trim().equals("")) {
                        Object cellData = ExcelUtils.cellProcessor(field, cell);
                        boolean acc = field.isAccessible();
                        field.setAccessible(true);
                        field.set(entry, cellData);
                        field.setAccessible(acc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            entries.add((T) entry);
        }

        return entries;
    }


    @Override
    public <T> List<T> importXSLXStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return null;
    }

    @Override
    public <T> List<T> importStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return null;
    }
}
