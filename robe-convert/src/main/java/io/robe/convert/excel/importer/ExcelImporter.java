package io.robe.convert.excel.importer;

import io.robe.convert.common.Importer;
import io.robe.convert.common.OnItemHandler;
import io.robe.convert.common.annotation.Convert;
import io.robe.convert.excel.parsers.Parsers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public abstract class ExcelImporter<T> extends Importer<T> {

    private boolean hasTitleRow;

    public ExcelImporter(Class dataClass) {
        super(dataClass);
    }

    public ExcelImporter(Class dataClass, boolean hasTitleRow) {
        super(dataClass);
        this.hasTitleRow = hasTitleRow;
    }

    public boolean hasTitleRow() {
        return hasTitleRow;
    }


    public List<T> importStream(InputStream inputStream, Workbook workbook) throws Exception {

        final List<T> list = new LinkedList<T>();

        DefaultOnItemHandler handler = new DefaultOnItemHandler(list);

        this.importStream(inputStream, workbook, handler);

        return list;
    }

    public void importStream(InputStream inputStream, Workbook workbook, OnItemHandler handler) throws Exception {

        Collection<FieldEntry> fields = getFields(getDataClass());

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        if (hasTitleRow() && rowIterator.hasNext())
            rowIterator.next();

        while (rowIterator.hasNext()) {
            T entry = (T) getDataClass().newInstance();
            fields.iterator().next();
            Row row = rowIterator.next();

            int cellCount = 0;
            for (FieldEntry fieldEntry : fields) {
                Field field = fieldEntry.getValue();
                Cell cell = row.getCell(cellCount++);
                Convert cfAnn = field.getAnnotation(Convert.class);
                try {
                    if (cfAnn.optional()) {

                        Object cellData;
                        if (cell != null) {
                            if (!(field.getType() instanceof Class && (field.getType()).isEnum())) {
                                cellData = Parsers.valueOf(field.getType().getSimpleName().toUpperCase(Locale.ENGLISH)).getParser().parse(cell.toString(), field);

                            } else {
                                cellData = Parsers.valueOf("ENUMTYPES").getParser().parse(cell, field);
                            }

                            checkFieldLength(cfAnn, cell, field, row);

                            boolean acc = field.isAccessible();
                            field.setAccessible(true);
                            field.set(entry, cellData);
                            field.setAccessible(acc);
                        }
                    } else {
                        if (cell != null && !cell.toString().trim().equals("")) {

                            Object cellData;

                            if (!(field.getType() instanceof Class && (field.getType()).isEnum())) {
                                cellData = Parsers.valueOf(field.getType().getSimpleName().toUpperCase(Locale.ENGLISH)).getParser().parse(cell.toString(), field);

                            } else {
                                cellData = Parsers.valueOf("ENUMTYPES").getParser().parse(cell, field);
                            }

                            checkFieldLength(cfAnn, cell, field, row);

                            boolean acc = field.isAccessible();
                            field.setAccessible(true);
                            field.set(entry, cellData);
                            field.setAccessible(acc);
                        } else {
                            throw new Exception("Exception at :" + row.getRowNum() + ". row and " + cellCount + ". cell ; " + field.getName() + " property can't be  null or empty ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            handler.onItem(entry);
        }
    }

    private void checkFieldLength(Convert cfAnn, Cell cell, Field field, Row row) throws Exception {

        if (cfAnn.minLength() > -1) {
            if ((int) cell.getNumericCellValue() < cfAnn.minLength()) {
                throw new Exception("in row " + row.getRowNum() + ", " + field.getName() + " field too short " + "(" + (int) cell.getNumericCellValue() + ")" + " min length : " + cfAnn.minLength());
            }
        }
        if (cfAnn.maxLength() > -1) {
            if ((int) cell.getNumericCellValue() > cfAnn.maxLength()) {
                throw new Exception("in row " + row.getRowNum() + " " + field.getName() + " too long " + "(" + (int) cell.getNumericCellValue() + ")" + " max length : " + cfAnn.maxLength());
            }
        }
    }

}
