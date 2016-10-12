package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class UserDTOTest {

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
