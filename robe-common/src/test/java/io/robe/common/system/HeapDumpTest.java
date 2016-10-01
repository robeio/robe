package io.robe.common.system;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class HeapDumpTest {
    File file;

    @Before
    public void setUp() throws Exception {
        file = HeapDump.dump(true);

    }

    @Test
    public void dump() throws Exception {
        assertTrue(file.exists());
    }
}
