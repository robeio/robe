package io.robe.convert.excel.exporter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class ExcelExporter extends IsExcelExporter {

    @Override
    public <T> void exportStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

    }

    @Override
    public <T> void exportXSLStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
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
        //TODO sheet name maybe must be pojo Name
        Sheet sheet = workbook.createSheet("Sheet 1");

        Row row = sheet.createRow(0);

        for (int sheetHeaderRow = 0; sheetHeaderRow < fieldNames.length; sheetHeaderRow++) {
            row.createCell(sheetHeaderRow).setCellValue(fieldNames[sheetHeaderRow]);
        }

        for (int entry = 0; entry < list.size(); entry++) {
            Field[] fieldsOfEntry = list.get(entry).getClass().getDeclaredFields();
            Row entryRow = sheet.createRow(entry + 1);

            for (int field = 0; field < fieldsOfEntry.length; field++) {
                boolean acc = fieldsOfEntry[field].isAccessible();
                fieldsOfEntry[field].setAccessible(true);
                entryRow.createCell(field).setCellValue(String.valueOf(fieldsOfEntry[field].get(list.get(entry))));
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

    @Override
    public <T> void exportXSLXStream(Class clazz, OutputStream outputStream, List<T> list) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

    }


    public String[] getFieldNames(Class clazz, String[] fieldNames) {
        Collection<Field> fields = getFields(clazz);

        int fieldNameCount = 0;
        for (Field field : fields) {
            fieldNames[fieldNameCount++] = field.getName();
        }

        return fieldNames;
    }
}
