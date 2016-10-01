package io.robe.common.utils.file;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by recep on 02/10/16.
 */
@FixMethodOrder
public class ListFilesUtilTest {
    ListFilesUtil listFilesUtil;

    @Before
    public void setUp() throws Exception {
        listFilesUtil = new ListFilesUtil();

    }

    @Test
    public void listFilesAndFolders() throws Exception {
        assertTrue(ListFilesUtil.listFilesAndFolders("/").size() > 0);
        assertEquals(0, ListFilesUtil.listFilesAndFolders("/test", ".*").size());
    }

    @Test
    public void listFiles() throws Exception {
        assertTrue(ListFilesUtil.listFiles("/").size() > 0);
        assertEquals(0, ListFilesUtil.listFiles("/test", ".*").size());
    }

    @Test
    public void listFolders() throws Exception {
        assertTrue(ListFilesUtil.listFolders("/").size() > 0);
        assertTrue(ListFilesUtil.listFolders("/", "").size() > 0);
        assertEquals(0, ListFilesUtil.listFolders("/test", ".*").size());
    }

    @Test
    public void listFilesAndFilesSubDirectories() throws Exception {
        assertTrue(ListFilesUtil.listFilesAndFilesSubDirectories("/", ".*").size() > 0);
        assertTrue(ListFilesUtil.listFilesAndFilesSubDirectories("/test").size() == 0);
        assertEquals(0, ListFilesUtil.listFilesAndFilesSubDirectories("/test", ".*").size());
    }

    @Test
    public void listFilesAndFilesSubDirectoriesByRegex() throws Exception {
        assertTrue(ListFilesUtil.listFilesAndFilesSubDirectoriesByRegex("/test", "").size() == 0);
    }

    @Test
    public void listFilesAndDirectoriesTree() throws Exception {
        assertTrue(ListFilesUtil.listFilesAndDirectoriesTree("/", ".*").size() > 0);
        assertTrue(ListFilesUtil.listFilesAndDirectoriesTree("/test").size() == 0);
        assertEquals(0, ListFilesUtil.listFilesAndDirectoriesTree("/test", ".*").size());
    }

    @Test
    public void listFilesAndDirectoriesTreeByRegex() throws Exception {
        assertTrue(ListFilesUtil.listFilesAndDirectoriesTreeByRegex("/test", "").size() == 0);
    }
}
