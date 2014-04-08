package io.robe.convert.excel.importer;

import io.robe.convert.IsImporter;
import io.robe.convert.OnItemHandler;
import io.robe.convert.excel.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class XLSImporter extends IsImporter {
    boolean isFirstRowHeader = false;

    public XLSImporter(boolean isFirstRowHeader) {
        this.isFirstRowHeader = isFirstRowHeader;
    }

    @Override
    public <T> List<T> importStream(Class clazz, InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        final List<T> entries = new LinkedList<T>();

        OnItemHandler<T> handler = new OnItemHandler<T>() {
            @Override
            public void onItem(T entry) {
                entries.add((T) entry);
            }
        };

        return entries;
    }

    @Override
    public <T> void importStream(Class clazz, InputStream inputStream, OnItemHandler handler) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        Collection<Field> fields = getFields(clazz);

        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        if (isFirstRowHeader)
            rowIterator.next();

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
            handler.onItem(entry);
        }

    }
}
