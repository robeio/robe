package io.robe.convert.excel.exporter;

import io.robe.convert.common.Exporter;
import io.robe.convert.common.annotation.Convert;
import io.robe.convert.excel.parsers.IsParser;
import io.robe.convert.excel.parsers.Parsers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;


public abstract class ExcelExporter<T> extends Exporter<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExporter.class);

    private boolean hasTitleRow;
    private Collection<FieldEntry> fields = null;
    private String[] fieldNames = null;

    public ExcelExporter(Class dataClass) {
        super(dataClass);
    }

    public ExcelExporter(Class dataClass, boolean hasTitleRow) {
        super(dataClass);
        this.hasTitleRow = hasTitleRow;

        fields = getFields(getDataClass());
        fieldNames = new String[fields.size()];
    }

    public boolean hasTitleRow() {
        return hasTitleRow;
    }

    public void exportStream(OutputStream outputStream, Iterator<T> iterator, Workbook workbook) throws IOException, ClassNotFoundException, IllegalAccessException {
        if (iterator == null) {
            throw new NullPointerException("List can not be null or empty.");
        }
        int fnIndex = 0;
        for (FieldEntry fieldEntry : fields) {
            Field field = fieldEntry.getValue();
            Convert cfAnn = field.getAnnotation(Convert.class);
            if (isSuitable(cfAnn)) {
                fieldNames[fnIndex++] = cfAnn.title().equals("") ? field.getName() : cfAnn.title();
            }
        }

        Sheet sheet = workbook.createSheet(getDataClass().getSimpleName());


        int entry = 0;

        if (hasTitleRow()) {
            LOGGER.debug("Exporting title row.");
            Row row = sheet.createRow(0);
            for (int sheetHeaderRow = 0; sheetHeaderRow < fieldNames.length; sheetHeaderRow++) {
                row.createCell(sheetHeaderRow).setCellValue(fieldNames[sheetHeaderRow]);
            }
            entry++;
        }


        while (iterator.hasNext()) {
            T item = iterator.next();
            LOGGER.debug("Exporting Row " + entry);
            Row entryRow = sheet.createRow(entry++);
            int fieldIndex = 0;
            for (FieldEntry fieldEntry : fields) {
                Field field = fieldEntry.getValue();
                Convert cfAnn = field.getAnnotation(Convert.class);
                if (isSuitable(cfAnn)) {
                    boolean initialAccessible = field.isAccessible();
                    field.setAccessible(true);
                    Cell cell = entryRow.createCell(fieldIndex++);
                    IsParser parser;
                    if (!(field.getType() != null && (field.getType()).isEnum())) {
                        parser = Parsers.valueOf(field.getType().getSimpleName().toUpperCase(Locale.ENGLISH)).getParser();
                    } else {
                        parser = Parsers.valueOf("ENUMTYPES").getParser();
                    }
                    parser.setCell(field.get(item), cell, field);
                    field.setAccessible(initialAccessible);

                }
            }
            LOGGER.debug("Exported Column Size: " + fieldIndex);
        }

        try {
            LOGGER.info("Finalizing Excel Document. Size: " + entry);
            workbook.write(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while writing.");
        }
    }
}
