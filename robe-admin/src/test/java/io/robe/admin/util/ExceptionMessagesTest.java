package io.robe.admin.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 11/10/2016.
 */
public class ExceptionMessagesTest {
    @Test
    public void ExceptionMessages() {
        Assert.assertTrue(ExceptionMessages.CANT_BE_NULL.toString().equals(" cannot be null."));
        Assert.assertTrue(ExceptionMessages.NOT_EXISTS.toString().equals(" not exists."));
    }
}
