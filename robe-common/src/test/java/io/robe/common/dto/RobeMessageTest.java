package io.robe.common.dto;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 28/12/2016.
 */
public class RobeMessageTest {
    @Test
    public void test() {
        RobeMessage.Builder builder = new RobeMessage.Builder();
        builder.status(500);
        Assert.assertEquals(500, builder.build().getStatus());
        builder.message("message should be same");
        Assert.assertEquals("message should be same", builder.build().getMessage());
        String id = System.currentTimeMillis() + "";
        builder.id(id);
        Assert.assertEquals(id, builder.build().getId());
        builder.code("12345");
        Assert.assertEquals("12345", builder.build().getCode());
        builder.moreInfo("https://github.com/robeio/robe");
        Assert.assertEquals("https://github.com/robeio/robe", builder.build().getMoreInfo());
    }
}