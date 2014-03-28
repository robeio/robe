package io.robe.common.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by serayuzgur on 20/03/14.
 */
public class FileOperationsTest {
    @Test
    public void testWriteToTemp() throws Exception {
        String testSentence = "asdasdaiğçşsdasdasdasdasd";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testSentence.getBytes("UTF-8"));
        File file = FileOperations.writeToTemp(inputStream);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[testSentence.getBytes("UTF-8").length];
        int i = 0;
        fileInputStream.read(buffer);
        fileInputStream.close();
        assert new String(buffer,"UTF-8").equals(testSentence);

    }

    @Test
    public void testWriteToApplicationHome() throws Exception {
        String testSentence = "asdasdaiğçşsdasdasdasdasd";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testSentence.getBytes("UTF-8"));
        File file = FileOperations.writeToApplicationHome("test",inputStream);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[testSentence.getBytes("UTF-8").length];
        int i = 0;
        fileInputStream.read(buffer);
        fileInputStream.close();
        assert new String(buffer,"UTF-8").equals(testSentence);

    }
}
