package io.robe.convert.excel.exporter;


import io.robe.convert.IsExporter;
import io.robe.convert.excel.parsers.IsParser;
import io.robe.convert.excel.parsers.Parsers;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class XLSExporter extends IsExporter {
    boolean isFirstRowHeader = false;

    public XLSExporter(boolean isFirstRowHeader) {
        this.isFirstRowHeader = isFirstRowHeader;
    }

    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (list == null) {
            throw new NullPointerException("List can not be null or empty.");
        }

        Collection<Field> fields = getFields(clazz);
        String[] fieldNames = new String[fields.size()];

        int fieldNameCount = 0;
        for (Field field : fields) {
            fieldNames[fieldNameCount++] = field.getName();
        }

        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet(clazz.getSimpleName());

        int startIndex = 0;
        if (isFirstRowHeader) {
            Row row = sheet.createRow(0);
            for (int sheetHeaderRow = 0; sheetHeaderRow < fieldNames.length; sheetHeaderRow++) {
                row.createCell(sheetHeaderRow).setCellValue(fieldNames[sheetHeaderRow]);
            }
            startIndex++;
        }

        for (int entry = 0; entry < list.size(); entry++) {
            Object instanceOfEntry = list.get(entry);
            Field[] fieldsOfEntry = instanceOfEntry.getClass().getDeclaredFields();
            Row entryRow = sheet.createRow(entry + startIndex);
            for (int field = 0; field < fieldsOfEntry.length; field++) {
                boolean acc = fieldsOfEntry[field].isAccessible();
                fieldsOfEntry[field].setAccessible(true);
                Cell cell = entryRow.createCell(field);
                IsParser parser = Parsers.valueOf(fieldsOfEntry[field].getType().getSimpleName().toUpperCase(Locale.ENGLISH)).getParser();
                parser.setCell(fieldsOfEntry[field].get(instanceOfEntry), cell, fieldsOfEntry[field]);
                fieldsOfEntry[field].setAccessible(acc);
            }
        }

        try {
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while writing.");
        }
    }
}
