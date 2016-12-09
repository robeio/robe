package io.robe.common.utils;

import io.robe.common.TestUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Created by hasanmumin on 26/09/16.
 */
public class ValidationsTest {

    @Test
    public void constructor() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestUtils.privateConstructor(Validations.class);
    }

    @Test
    public void isEmptyOrNull() {
        assertTrue(Validations.isEmptyOrNull(null));
        assertTrue(Validations.isEmptyOrNull(""));
        assertFalse(Validations.isEmptyOrNull("robe.io"));
    }
}
