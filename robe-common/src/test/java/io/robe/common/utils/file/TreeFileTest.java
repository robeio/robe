package io.robe.common.utils.file;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 02/10/16.
 */
@FixMethodOrder
public class TreeFileTest {
    TreeFile treeFile;
    File file;

    @Before
    public void setUp() throws Exception {
        file = new File("test");
        treeFile = new TreeFile(file);

    }

    @Test
    public void test() throws Exception {
        assertEquals(file.isFile(), treeFile.isFile());

    }
}
