package io.robe.common.dto;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class BasicPairTest {
    BasicPair pair;

    @Before
    public void setUp() throws Exception {
        pair = new BasicPair("Basic", "Pair");

    }

    @Test
    public void contructorAndToString() throws Exception {
        BasicPair basicPair = new BasicPair();
        assertNull(basicPair.getName());

        assertEquals(pair.getName() + "=" + pair.getValue(), pair.toString());

    }
}
