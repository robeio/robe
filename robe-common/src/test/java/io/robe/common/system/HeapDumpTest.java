package io.robe.common.system;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class HeapDumpTest {

    @Test
    public void dump() throws Exception {
        assertNotNull(HeapDump.dump(false));
    }
}
