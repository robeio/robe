package io.robe.convert.excel;

import io.robe.convert.excel.parsers.Parsers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 20/10/2016.
 */
public class ParsersTest {

    @Test
    public void toStringTest() {
        Assert.assertTrue(Parsers.BIGDECIMAL.toString().equals("java.math.BigDecimal"));
        Assert.assertTrue(Parsers.BOOLEAN.toString().equals("java.lang.Boolean"));
        Assert.assertTrue(Parsers.BYTE.toString().equals("java.lang.Byte"));
        Assert.assertTrue(Parsers.DOUBLE.toString().equals("java.lang.Double"));
        Assert.assertTrue(Parsers.INT.toString().equals("int"));
        Assert.assertTrue(Parsers.INTEGER.toString().equals("java.lang.Integer"));
        Assert.assertTrue(Parsers.LONG.toString().equals("java.lang.Long"));
        Assert.assertTrue(Parsers.STRING.toString().equals("java.lang.String"));
        Assert.assertTrue(Parsers.DATE.toString().equals("java.util.Date"));
        Assert.assertTrue(Parsers.ENUM.toString().equals("java.lang.String"));
    }
}
