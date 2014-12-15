package io.robe.convert.excel.parsers;

import org.apache.poi.ss.usermodel.Cell;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.util.CsvContext;

import java.lang.reflect.Field;

import static java.lang.Enum.valueOf;

public class ParseEnum extends CellProcessorAdaptor implements IsParser {
    Class<? extends Enum> enumType;

    @Override
    public Object parse(Object o, Field field) {
        if (!field.getType().isEnum())
            return null;
        Class<? extends Enum> type = (Class<? extends Enum>) field.getType();
        Enum anEnum = valueOf(type, o.toString());
        return anEnum;
    }

    @Override
    public void setCell(Object o, Cell cell, Field field) {
        Enum anEnum = (Enum) o;
        if (anEnum != null) {
            cell.setCellValue(anEnum.name());
        }
    }

    @Override
    public Object execute(Object value, CsvContext context) {
        validateInputNotNull(value, context);

        if (value instanceof Enum) {
            String result = value.toString();
            return next.execute(result, context);
        } else {
            Enum result = valueOf(enumType,value.toString());
            return next.execute(result, context);
        }
    }

    public void setEnumType(Class<? extends Enum> enumType) {
        this.enumType = enumType;
    }
}
