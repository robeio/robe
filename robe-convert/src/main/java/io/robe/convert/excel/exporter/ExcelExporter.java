package io.robe.convert.excel.exporter;

import io.robe.convert.IsExporter;
import io.robe.convert.excel.parsers.IsParser;
import io.robe.convert.excel.parsers.Parsers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;


public abstract class ExcelExporter extends IsExporter {

    public <T> void exportStream(Class clazz, OutputStream outputStream, Iterator<T> iterator, boolean isFirstRowHeader, Workbook workbook) throws IOException, ClassNotFoundException, IllegalAccessException {
        if (iterator == null) {
            throw new NullPointerException("List can not be null or empty.");
        }

        Collection<Field> fields = getFields(clazz);
        String[] fieldNames = new String[fields.size()];

        int fieldNameCount = 0;
        for (Field field : fields) {
            fieldNames[fieldNameCount++] = field.getName();
        }

        Sheet sheet = workbook.createSheet(clazz.getSimpleName());

        int startIndex = 0;

        if (isFirstRowHeader) {
            Row row = sheet.createRow(0);
            for (int sheetHeaderRow = 0; sheetHeaderRow < fieldNames.length; sheetHeaderRow++) {
                row.createCell(sheetHeaderRow).setCellValue(fieldNames[sheetHeaderRow]);
            }
            startIndex++;
        }


        int entry = 0;
        while (iterator.hasNext()) {
            T item = iterator.next();
            Row entryRow = sheet.createRow(entry++ + startIndex);

            int fieldIndex = 0;
            for (Field field : fields) {
                boolean acc = field.isAccessible();
                field.setAccessible(true);
                Cell cell = entryRow.createCell(fieldIndex++);
                IsParser parser = Parsers.valueOf(field.getType().getSimpleName().toUpperCase(Locale.ENGLISH)).getParser();
                parser.setCell(field.get(item), cell, field);
                field.setAccessible(acc);
            }
        }

        try {
            workbook.write(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while writing.");
        }
    }
}
