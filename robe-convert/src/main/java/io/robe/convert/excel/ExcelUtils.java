package io.robe.convert.excel;

//import io.robe.convert.SimpleDateFormat;
import io.robe.convert.excel.parsers.*;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;
        import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtils {

    public static final Map<String, IsParser> cellMap = new HashMap<String, IsParser>();

    static {
        IsParser isParser = new IntParser();
        cellMap.put(Integer.class.getName(), isParser);
        cellMap.put(int.class.getName(), isParser);

        isParser = new LongParser();
        cellMap.put(Long.class.getName(), isParser);
        cellMap.put(long.class.getName(), isParser);

        isParser = new DoubleParser();
        cellMap.put(Double.class.getName(), isParser);
        cellMap.put(double.class.getName(), isParser);

        isParser = new ByteParser();
        cellMap.put(Byte.class.getName(), isParser);
        cellMap.put(byte.class.getName(), isParser);

        isParser = new BoolParser();
        cellMap.put(Boolean.class.getName(), isParser);
        cellMap.put(boolean.class.getName(), isParser);

        isParser = new BigDecimalParser();
        cellMap.put(BigDecimal.class.getName(), isParser);

        cellMap.put(String.class.getName(), new StringParser());
        cellMap.put(Date.class.getName(), new DateParser());
    }

    public static Object cellProcessor(Field field, Cell cell) throws Exception {

        IsParser parser = cellMap.get(field.getType().getName());
        if (parser == null) {
            throw new RuntimeException("Unknown Type :" + field.getType().getName());
        }
        return parser.parse(cell.toString(),field);
    }
}
