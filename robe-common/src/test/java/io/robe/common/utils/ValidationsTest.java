package io.robe.common.utils;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class ValidationsTest {

    @Test
    public void isEmptyOrNull() throws Exception {
        assertTrue(Validations.isEmptyOrNull(null));
        assertTrue(Validations.isEmptyOrNull(" "));
    }
}
