package io.robe.common.utils.file;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;

/**
 * Created by recep on 02/10/16.
 */
@FixMethodOrder
public class TreeDataTest {
    TreeData data;

    @Before
    public void setUp() throws Exception {
        data = new TestData(new File("test"));
    }

    @Test
    public void isFile() throws Exception {
        assertFalse(data.isFile());
    }

    @Test
    public void isDirectory() throws Exception {
        assertFalse(data.isDirectory());
    }

    class TestData extends TreeData {
        public TestData(File file) {
            super(file);
        }
    }
}
