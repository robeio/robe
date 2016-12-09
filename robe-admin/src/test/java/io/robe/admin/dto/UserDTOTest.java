package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class UserDTOTest {
    User user;
    UserDTO dto;
    UserDTO dto2;

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setActive(true);
        user.setName("name");
        user.setEmail("mail");
        user.setSurname("surname");
        user.setPassword("password");
        user.setRoleOid("oid");
        dto = new UserDTO(user);
        dto2 = new UserDTO();

    }

    @Test
    public void getNewPassword() throws Exception {
        dto.setNewPassword("123");
        assertEquals("123", dto.getNewPassword());
    }

    @Test
    public void getTicket() throws Exception {
        dto.setTicket("ticket");
        assertEquals("ticket", dto.getTicket());
    }

    @Test
    public void getUsername() throws Exception {
        dto.setUsername("username");
        assertEquals("username", dto.getUsername());
    }

    @Test
    public void userDTO() {
        UserDTO dto = new UserDTO();
        dto.setNewPassword("123123");
        dto.setUsername("admin@robe.io");

        String ticket = UUID.randomUUID().toString();
        dto.setTicket(ticket);


        Assert.assertEquals(dto.getTicket(), ticket);
        Assert.assertEquals(dto.getNewPassword(), "123123");
        Assert.assertEquals(dto.getUsername(), "admin@robe.io");

    }

    @Test
    public void userDTOWithUser() {

        User user = new User();
        user.setEmail("admin@robe.io");
        user.setName("Admin");
        user.setSurname("Admin");
        UserDTO dto = new UserDTO(user);

        Assert.assertEquals(dto.getEmail(), user.getEmail());
        Assert.assertEquals(dto.getName(), user.getName());
        Assert.assertEquals(dto.getSurname(), user.getSurname());
    }

}
