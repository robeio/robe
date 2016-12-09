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
        Assert.assertEquals(expected, Strings.capitalizeFirstChar(data));

    }

    @Test
    public void unCapitalizeFirstChar() throws Exception {
        String expected = "id";
        String data = "Id";
        Assert.assertEquals(expected, Strings.unCapitalizeFirstChar(data));

    }

    @Test
    public void unCapitalizeFirstChar() throws Exception {
        String expected = "id";
        String data = "Id";
        Assert.assertEquals(expected, StringsOperations.unCapitalizeFirstChar(data));

    }

}