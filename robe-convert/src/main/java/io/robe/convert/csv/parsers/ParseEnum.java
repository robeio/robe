package io.robe.convert.csv.parsers;


import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.util.CsvContext;

import static java.lang.Enum.valueOf;

public class ParseEnum extends CellProcessorAdaptor {
    Class<? extends Enum> enumType;


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
