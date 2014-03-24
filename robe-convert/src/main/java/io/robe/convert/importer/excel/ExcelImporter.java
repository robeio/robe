package io.robe.convert.importer.excel;

import io.robe.convert.FileEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ExcelImporter extends AbstractExcelImporter {

    @Override
    public <T> List<T> importExcelStream(FileEnum fileEnum, InputStream inputStream, Class pojoClass) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        Collection<Field> fields = getFields(pojoClass);
        String[] fieldNames = new String[fields.size()];

        Workbook workbook = null;

        if (fileEnum == FileEnum.XLS) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileEnum == FileEnum.XSLX) {
            workbook = new XSSFWorkbook(inputStream);
        }

        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();


        return null;
    }

    public String cellProcessor(Cell cell) {

        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return ((Double) cell.getNumericCellValue()).longValue() + "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue() + "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            return cell.getErrorCellValue() + "";
        } else {
            return null;
        }
    }
}
