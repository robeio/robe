package io.robe.convert.csv;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.robe.convert.MappingProperty;
import io.robe.convert.csv.supercsv.ParseBigDecimalFix;
import io.robe.convert.csv.supercsv.ParseDateFix;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class CSVUtil {

    static CellProcessor[] convertFieldsToCellProcessors(Collection<Field> fields, String[] fieldNames) {
        CellProcessor[] processors = new CellProcessor[fields.size()];
        int i = 0;
        for (Field field : fields) {
            MappingProperty an = field.getAnnotation(MappingProperty.class);
            if(an == null){
                continue;
            }
            CellProcessorAdaptor a = decideAdaptor(field);
            CellProcessor p = null;
            if (an.optional()) {
                if (a != null) {
                    p = new Optional(a);
                } else {
                    p = new Optional();
                }
            } else {
                if (a != null) {
                    p = new NotNull(a);
                } else {
                    p = new NotNull();
                }
            }
            fieldNames[i] = field.getName();
            processors[i++] = p;
        }
        return processors;
    }

    static CellProcessorAdaptor decideAdaptor(Field field) {
        String fieldType = field.getType().toString();
        if (fieldType.equals("int")) {
            return new ParseInt();
        } else if (fieldType.equals("long")) {
            return new ParseLong();
        } else if (fieldType.equals("double")) {
            return new ParseDouble();
        } else if (fieldType.equals(BigDecimal.class.toString())) {
            return new ParseBigDecimalFix();
        } else if (fieldType.equals(Date.class.toString())) {
            if(field.getAnnotation(JsonFormat.class) != null){
                String format = field.getAnnotation(JsonFormat.class).pattern();
                return new ParseDateFix(format);
            }else{
                throw  new RuntimeException("Date type must have SimpleDateFormat annotation with a valid format.");
            }
        } else if (fieldType.equals("char")) {
            return new ParseChar();
        } else {
            return null;
        }
    }
}
