package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class ActionLogTest {
    ActionLog entity = new ActionLog();

    @Test
    public void getRemoteAddr() throws Exception {
        entity.setRemoteAddr("RemoteAddr");
        assertEquals("RemoteAddr", entity.getRemoteAddr());
    }

    @Test
    public void getActionType() throws Exception {
        entity.setActionType("ActionType");
        assertEquals("ActionType", entity.getActionType());
    }

    @Test
    public void getActionTime() throws Exception {
        Date date = new Date();
        entity.setActionTime(date);
        assertEquals(date.getTime(), entity.getActionTime().getTime());
    }

    @Test
    public void getDescription() throws Exception {
        entity.setDescription("Description");
        assertEquals("Description", entity.getDescription());
    }

    @Test
    public void getAdditionalData() throws Exception {
        entity.setAdditionalData("AdditionalData");
        assertEquals("AdditionalData", entity.getAdditionalData());
    }

    @Test
    public void isPositive() throws Exception {
        entity.setPositive(true);
        assertTrue(entity.isPositive());
    }

    // @Test
    public void constructorAndToString() throws Exception {
        ActionLog log1 = new ActionLog("ActionType", true);
        log1.setDescription("Description");
        ActionLog log2 = new ActionLog("ActionType", "Description", true);
        assertEquals(log1.getDescription(), log2.getDescription());

        ActionLog log3 = new ActionLog("ActionType", "Description", "AdditionalData", true, "RemoteAddr");
        assertNotEquals(entity, log3);

        assertEquals(log1.toString(), log2.toString());

    }
}
