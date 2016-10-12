package io.robe.admin.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 11/10/2016.
 */
public class ExceptionMessagesTest {
    @Test
    public void exceptionMessages() {
        Assert.assertTrue(ExceptionMessages.CANT_BE_NULL.toString().equals(" cannot be null."));
        Assert.assertTrue(ExceptionMessages.NOT_EXISTS.toString().equals(" not exists."));

        ExceptionMessages[] values = ExceptionMessages.values();
        Assert.assertTrue(values.length == 2);

        ExceptionMessages exceptionMessage = ExceptionMessages.valueOf(ExceptionMessages.CANT_BE_NULL.name());

        Assert.assertTrue(exceptionMessage.toString().equals(" cannot be null."));
    }
}
