package io.robe.common.dto;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 26/09/16.
 */
public class BasicPairTest {

    @Test
    public void instance() {
        BasicPair basicPair = new BasicPair("foo", "bar");
        Assert.assertEquals(basicPair.getName(), "foo");
        Assert.assertEquals(basicPair.getValue(), "bar");
        Assert.assertTrue(basicPair.equals(basicPair));
    }

}
