package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class UserTest {
    User entity = new User();

    @Test
    public void getEmail() throws Exception {
        entity.setEmail("abc@abc.com");
        assertEquals("abc@abc.com", entity.getEmail());
    }

    @Test
    public void getName() throws Exception {
        entity.setName("Name");
        assertEquals("Name", entity.getName());
    }

    @Test
    public void getSurname() throws Exception {
        entity.setSurname("Surname");
        assertEquals("Surname", entity.getSurname());
    }

    @Test
    public void getPassword() throws Exception {
        entity.setPassword("123123");
        assertEquals("123123", entity.getPassword());
    }

    @Test
    public void getRoleOid() throws Exception {
        entity.setRoleOid("123123");
        assertEquals("123123", entity.getRoleOid());
    }

    @Test
    public void getRoleId() throws Exception {
        entity.setRoleOid("123123");
        assertEquals("123123", entity.getRoleId());
    }

    @Test
    public void isActive() throws Exception {
        entity.setActive(true);
        assertTrue(entity.isActive());
    }

    @Test
    public void getUserId() throws Exception {
        entity.setOid("123123");
        assertEquals("123123", entity.getUserId());
    }

    @Test
    public void getUsername() throws Exception {
        entity.setEmail("abc@abc.com");
        assertEquals("abc@abc.com", entity.getUsername());
    }

    @Test
    public void getLastLoginTime() throws Exception {
        Date date = new Date();
        entity.setLastLoginTime(date);
        assertEquals(date.getTime(), entity.getLastLoginTime().getTime());
    }

    @Test
    public void getLastLogoutTime() throws Exception {
        Date date = new Date();
        entity.setLastLogoutTime(date);
        assertEquals(date, entity.getLastLogoutTime());
    }

    @Test
    public void getFailCount() throws Exception {
        entity.setFailCount(123);
        assertEquals(123, entity.getFailCount());
    }

    @Test
    public void testToString() throws Exception {
        User user = new User();
        user.setOid("123123");
        User user1 = new User();
        user1.setOid("123123");

        assertEquals(user.toString(), user1.toString());

    }
}
