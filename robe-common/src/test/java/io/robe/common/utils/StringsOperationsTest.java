package io.robe.common.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by serayuzgur on 28/05/16.
 */
public class StringsOperationsTest {
    @Test
    public void capitalizeFirstChar() throws Exception {
        String expected = "Id";
        String data = "id";
        Assert.assertEquals(expected, StringsOperations.capitalizeFirstChar(data));

    }

}