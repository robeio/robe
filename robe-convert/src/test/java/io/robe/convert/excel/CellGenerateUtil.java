package io.robe.convert.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by hasanmumin on 27/10/2016.
 */
public class CellGenerateUtil {

    public static Cell create() {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("New-Sheet");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        return cell;
    }
}
