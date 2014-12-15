package io.robe.convert;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by serayuzgur on 10/04/14.
 */
public class TestData {
    private static List<SamplePojo> pojos = new LinkedList<SamplePojo>();

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    static {
        try {
            pojos.add(new SamplePojo(1, "Seray", "Uzgur", 11, 111, BigDecimal.valueOf(1111), FORMAT.parse("01.01.2014"),SampleEnum.SAMPLE1));
            pojos.add(new SamplePojo(2, "Kaan", "Alkim", 12, 112, BigDecimal.valueOf(1112), FORMAT.parse("02.01.2014"),SampleEnum.SAMPLE2));
            pojos.add(new SamplePojo(3, "Sinan", "Selimogli", 13, 113, BigDecimal.valueOf(1113), FORMAT.parse("03.01.2014"),SampleEnum.SAMPLE1));
            pojos.add(new SamplePojo(4, "Kamil", "Bukum", 14, 114, BigDecimal.valueOf(1114), FORMAT.parse("04.01.2014"),SampleEnum.SAMPLE2));
            pojos.add(new SamplePojo(5, "Hasan", "Mumin", 15, 115, BigDecimal.valueOf(1115), FORMAT.parse("05.01.2014"),SampleEnum.SAMPLE1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<SamplePojo> getData() {
        return pojos;
    }

    private TestData() {
    }
}
