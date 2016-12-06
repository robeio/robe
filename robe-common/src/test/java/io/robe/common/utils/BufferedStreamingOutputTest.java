package io.robe.common.utils;

import io.robe.common.service.stream.BufferedStreamingOutput;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.StreamingOutput;
import java.io.*;

/**
 * Created by hasanmumin on 26/09/16.
 */
public class BufferedStreamingOutputTest {

    @Test
    public void write() throws IOException {

        String testSentence = "robe";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testSentence.getBytes("UTF-8"));
        File file = Files.writeToTemp(inputStream);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        StreamingOutput stream = new BufferedStreamingOutput(bufferedInputStream, 5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        stream.write(outputStream);
        Assert.assertTrue(outputStream.size() == 4);


        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        stream = new BufferedStreamingOutput(bufferedInputStream, 3);

        outputStream = new ByteArrayOutputStream();
        stream.write(outputStream);
        Assert.assertTrue(outputStream.size() == 4);

    }
}
