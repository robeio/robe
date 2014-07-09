package io.robe.convert.excel.importer;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.robe.convert.IsImporter;
import io.robe.convert.MappingProperty;
import io.robe.convert.OnItemHandler;
import io.robe.convert.excel.parsers.Parsers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public abstract class ExcelImporter extends IsImporter {

    public <T> List<T> importStream(Class clazz, InputStream inputStream, boolean isFirstRowHeader, Workbook workbook) throws Exception {

        final List<T> entries = new LinkedList<T>();

        OnItemHandler<T> handler = new OnItemHandler<T>() {
            @Override
            public void onItem(T entry) {
                entries.add((T) entry);
            }
        };

        this.importStream(clazz, inputStream, isFirstRowHeader, workbook, handler);

        return entries;
    }

    public <T> void importStream(Class clazz, InputStream inputStream, boolean isFirstRowHeader, Workbook workbook, OnItemHandler handler) throws Exception {

        Collection<Field> fields = getFields(clazz);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        if (isFirstRowHeader && rowIterator.hasNext())
            rowIterator.next();

        while (rowIterator.hasNext()) {
            T entry = (T) clazz.newInstance();
            fields.iterator().next();
            Row row = rowIterator.next();

            int cellCount = 0;
            for (Field field : fields) {
                Cell cell = row.getCell(cellCount++);
                Annotation fieldAnnotation = field.getAnnotation(MappingProperty.class);
                Annotation dateAnnotation = field.getAnnotation(JsonFormat.class);

                MappingProperty fieldMappingProperties = (MappingProperty) fieldAnnotation;

                try {
                    if (fieldMappingProperties.optional()) {
                        Object cellData = Parsers.valueOf(field.getType().getSimpleName().toUpperCase(Locale.ENGLISH)).getParser().parse(cell.toString(), field);

                        if (fieldMappingProperties.length() > -1) {
                            if (cell.toString().length() > fieldMappingProperties.length()) {
                                throw new Exception(field.getName() + " too long " + "(" + cell.toString() + ")" + " max length : " + fieldMappingProperties.length());
                            }
                        }
                        if (fieldMappingProperties.min() > -1) {
                            if ((int) cell.getNumericCellValue() < fieldMappingProperties.min()) {
                                throw new Exception("in row " + row.getRowNum() + ", " + field.getName() + " field too short " + "(" + (int) cell.getNumericCellValue() + ")" + " min length : " + fieldMappingProperties.min());
                            }
                        }

                        if (fieldMappingProperties.max() > -1) {
                            if ((int) cell.getNumericCellValue() > fieldMappingProperties.max()) {
                                throw new Exception("in row " + row.getRowNum() + " " + field.getName() + " too long " + "(" + (int) cell.getNumericCellValue() + ")" + " max length : " + fieldMappingProperties.max());
                            }
                        }

                        boolean acc = field.isAccessible();
                        field.setAccessible(true);
                        field.set(entry, cellData);
                        field.setAccessible(acc);
                    } else {
                        if (cell != null && !cell.toString().trim().equals("")) {
                            Object cellData = Parsers.valueOf(field.getType().getSimpleName().toUpperCase(Locale.ENGLISH)).getParser().parse(cell.toString(), field);

                            if (fieldMappingProperties.length() > -1) {
                                if (cell.toString().length() > fieldMappingProperties.length()) {
                                    throw new Exception(field.getName() + " too long " + "(" + cell.toString() + ")" + " max length : " + fieldMappingProperties.length());
                                }
                            }
                            if (fieldMappingProperties.min() > -1) {
                                if ((int) cell.getNumericCellValue() < fieldMappingProperties.min()) {
                                    throw new Exception("in row " + row.getRowNum() + ", " + field.getName() + " field too short " + "(" + (int) cell.getNumericCellValue() + ")" + " min length : " + fieldMappingProperties.min());
                                }
                            }
                            if (fieldMappingProperties.max() > -1) {
                                if ((int) cell.getNumericCellValue() > fieldMappingProperties.max()) {
                                    throw new Exception("in row " + row.getRowNum() + " " + field.getName() + " too long " + "(" + (int) cell.getNumericCellValue() + ")" + " max length : " + fieldMappingProperties.max());
                                }
                            }

                            boolean acc = field.isAccessible();
                            field.setAccessible(true);
                            field.set(entry, cellData);
                            field.setAccessible(acc);
                        } else {
                            throw new Exception("in row " + row.getRowNum() + " " + field.getName() + " mustn't null or empty " + "(" + (int) cell.getNumericCellValue() + ")");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            handler.onItem(entry);
        }
    }
}
