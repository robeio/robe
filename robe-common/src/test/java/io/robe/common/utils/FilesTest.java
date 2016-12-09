package io.robe.common.utils;

import io.robe.common.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by serayuzgur on 20/03/14.
 */
public class FilesTest {


    @Test
    public void constructor() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestUtils.privateConstructor(Files.class);
    }

    @Test
    public void writeToTemp() throws Exception {
        String testSentence = "asdasdaiğçşsdasdasdasdasd";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testSentence.getBytes("UTF-8"));
        File file = Files.writeToTemp(inputStream);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[testSentence.getBytes("UTF-8").length];
        fileInputStream.read(buffer);
        fileInputStream.close();
        assert new String(buffer, "UTF-8").equals(testSentence);

        file = Files.writeToTemp(null);
        assert file.length() == 0;

    }

    @Test
    public void writeFileToDirectory() {
        try {
            Files.writeFileToDirectory(new ByteArrayInputStream("asd".getBytes("UTF-8")), "/");
            Assert.fail("should be throw exception");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage() != null);
        }
    }

}
