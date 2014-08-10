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
            pojos.add(new SamplePojo(1, "Seray", "Uzgur", 1, 6, BigDecimal.ZERO, FORMAT.parse("01.01.2014"), SampleEnum.SAMPLE1, Sample2Enum.SAMPLE_2_ENUM));
            pojos.add(new SamplePojo(1, "Kaan", "Alkim", 2, 7, BigDecimal.ONE, FORMAT.parse("02.01.2014"), SampleEnum.SAMPLE2, Sample2Enum.SAMPLE_3_ENUM));
            pojos.add(new SamplePojo(1, "Sinan", "Selimogli", 3, 8, BigDecimal.ZERO, FORMAT.parse("03.01.2014"), SampleEnum.SAMPLE1, Sample2Enum.SAMPLE_2_ENUM));
            pojos.add(new SamplePojo(1, "Kamil", "Bukum", 4, 9, BigDecimal.ONE, FORMAT.parse("04.01.2014"), SampleEnum.SAMPLE2, Sample2Enum.SAMPLE_3_ENUM));
            pojos.add(new SamplePojo(1, "Hasan", "Mumin", 5, 10, BigDecimal.ZERO, FORMAT.parse("05.01.2014"), SampleEnum.SAMPLE1, Sample2Enum.SAMPLE_2_ENUM));
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
