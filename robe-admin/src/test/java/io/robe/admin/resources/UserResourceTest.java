package io.robe.admin.resources;

import com.google.common.hash.Hashing;
import io.robe.admin.hibernate.entity.User;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Created by hasanmumin on 13/10/2016.
 */
public class UserResourceTest extends BaseResourceTest<User> {
    @Override
    public String getPath() {
        return "users";
    }

    @Override
    public Class<User> getClazz() {
        return User.class;
    }

    @Override
    public void assertEquals(User model, User response) {
        Assert.assertEquals(model.getEmail(), response.getEmail());
        Assert.assertEquals(model.getName(), response.getName());
        Assert.assertEquals(model.getSurname(), response.getSurname());
        Assert.assertEquals(model.getPassword(), response.getPassword());
    }

    @Override
    public void assertEquals(User mergeInstance, User original, User response) {
        Assert.assertEquals(original.getEmail(), response.getEmail());
        Assert.assertEquals(original.getName(), response.getName());
        Assert.assertEquals(mergeInstance.getSurname(), response.getSurname());
        Assert.assertEquals(original.getPassword(), response.getPassword());
    }

    @Override
    public User instance() {
        User user = new User();
        user.setEmail("demo@robe.io");
        user.setName("Name");
        user.setSurname("Name");
        user.setPassword(Hashing.sha256().hashString("123123", StandardCharsets.UTF_8).toString());
        user.setActive(true);
        user.setFailCount(0);
        user.setRoleOid(UUID.randomUUID().toString().replaceAll("-", ""));
        return user;
    }

    @Override
    public User update(User response) {
        response.setName("Name1");
        return response;
    }

    @Override
    public User mergeInstance() {
        User user = new User();
        user.setSurname("Name2");
        return user;
    }
}
