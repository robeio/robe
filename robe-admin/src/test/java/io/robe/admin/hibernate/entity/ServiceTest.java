package io.robe.admin.hibernate.entity;

import io.robe.auth.data.entry.ServiceEntry;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class ServiceTest {
    Service entity = new Service();

    @Test
    public void getPath() throws Exception {
        entity.setPath("Path");
        assertEquals("Path", entity.getPath());
    }

    @Test
    public void getMethod() throws Exception {
        entity.setMethod(ServiceEntry.Method.DELETE);
        assertEquals(ServiceEntry.Method.DELETE, entity.getMethod());
    }

    @Test
    public void getGroup() throws Exception {
        entity.setGroup("Group");
        assertEquals("Group", entity.getGroup());
    }

    @Test
    public void getDescription() throws Exception {
        entity.setDescription("Destriction");
        assertEquals("Destriction", entity.getDescription());
    }

}
