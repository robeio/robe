package io.robe.admin.hibernate.dao;

import com.google.common.hash.Hashing;
import io.robe.admin.hibernate.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class UserDaoTest extends BaseDaoTest<User, UserDao> {

    private final String roleOid = UUID.randomUUID().toString().replaceAll("-", "");

    @Override
    public User instance() {
        User user = new User();
        user.setName("Admin");
        user.setSurname("Admin");
        user.setActive(true);
        user.setEmail("robe@robe.io");
        user.setRoleOid(roleOid);
        user.setPassword(Hashing.sha256().hashString("123123", StandardCharsets.UTF_8).toString());
        return user;
    }

    @Override
    public User update(User model) {
        model.setName("Admin-1");
        model.setSurname("Admin-1");
        return model;
    }

    @Test
    public void findByRoleId() {
        super.createFrom();
        List<User> users = dao.findByRoleId(roleOid);
        Assert.assertTrue(users.size() == 1);
        super.deleteFrom(users.get(0));
    }

    @Test
    public void changePassword() {
        User user = super.createFrom();

        String password = Hashing.sha256().hashString("321321", StandardCharsets.UTF_8).toString();

        Optional<User> response = (Optional<User>) dao.changePassword(user.getUsername(), password);

        Assert.assertTrue(response.isPresent());

        Assert.assertTrue(response.get().getPassword().equals(password));

        super.deleteFrom(response.get());

        password = Hashing.sha256().hashString("555555", StandardCharsets.UTF_8).toString();
        response = (Optional<User>) dao.changePassword("none@robe.io", password);
        Assert.assertFalse(response.isPresent());
    }
}
