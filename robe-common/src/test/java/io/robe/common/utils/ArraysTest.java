package io.robe.common.utils;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by kamilbukum on 11/11/16.
 */
public class ArraysTest {

    @Test
    public void isExist(){
        String[] arr = new String[]{"kamil","example"};
        boolean isExist = Arrays.isExist(arr, "example");
        assertTrue("Couldn't find element in " + arr , isExist);

        isExist = Arrays.isExist(arr, "exam");
        assertFalse("Found element in " + arr , isExist);
    }
}
