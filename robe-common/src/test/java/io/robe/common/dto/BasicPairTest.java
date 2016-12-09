package io.robe.common.dto;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Assert;


@FixMethodOrder
public class BasicPairTest {

    @Test
    public void instance() {
        BasicPair basicPair = new BasicPair("foo", "bar");
        Assert.assertEquals(basicPair.getName(), "foo");
        Assert.assertEquals(basicPair.getValue(), "bar");
        Assert.assertTrue(basicPair.equals(basicPair));
    }


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
