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
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static List<SamplePojo> pojos = new LinkedList<SamplePojo>();

    static {
        try {
            pojos.add(new SamplePojo(1, "Seray", "Uzgur", 11, 111, BigDecimal.valueOf(11111111111l), FORMAT.parse("01.01.2014"), SampleEnum.SAMPLE1, "false", "1"));
            pojos.add(new SamplePojo(2, "Kaan", "Alkim", 12, 112, BigDecimal.valueOf(11111111112l), FORMAT.parse("02.01.2014"), SampleEnum.SAMPLE2, "true", "2"));
            pojos.add(new SamplePojo(3, "Sinan", "Selimogli", 13, 113, BigDecimal.valueOf(11111111113l), FORMAT.parse("03.01.2014"), SampleEnum.SAMPLE1, "true", "3"));
            pojos.add(new SamplePojo(4, "Kamil", "Bukum", 14, 114, BigDecimal.valueOf(11111111114l), FORMAT.parse("04.01.2014"), SampleEnum.SAMPLE2, "true", "4"));
            pojos.add(new SamplePojo(5, "Hasan", "Mumin", 15, 115, BigDecimal.valueOf(11111111115l), FORMAT.parse("05.01.2014"), SampleEnum.SAMPLE1, "true", null));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private TestData() {
    }

    public static List<SamplePojo> getData() {
        return pojos;
    }
}
