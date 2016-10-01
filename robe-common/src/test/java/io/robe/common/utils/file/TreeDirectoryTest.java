package io.robe.common.utils.file;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 02/10/16.
 */
@FixMethodOrder
public class TreeDirectoryTest {
    TreeDirectory treeDirectory;

    @Before
    public void setUp() throws Exception {

        treeDirectory = new TreeDirectory(new File("test"));

    }

    @Test
    public void getChildList() throws Exception {
        ArrayList<TreeData> list = new ArrayList<>();
        list.add(new TestData(new File("test")));

        treeDirectory.setChildList(list);

        assertEquals(list, treeDirectory.getChildList());

    }

    class TestData extends TreeData {
        public TestData(File file) {
            super(file);
        }
    }
}
