package io.robe.admin.resources;

import io.robe.admin.RobeAdminTest;
import io.robe.admin.rest.Response;
import io.robe.admin.rest.RobeRestClient;
import io.robe.admin.rest.http.HttpRequest;
import io.robe.admin.rest.http.HttpRequestImpl;
import io.robe.hibernate.entity.BaseEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by hasanmumin on 03/10/16.
 */
public abstract class BaseResourceTest<T extends BaseEntity> extends RobeAdminTest {

    private RobeRestClient<T, String> entityClient;

    public abstract String getPath();

    public abstract Class<T> getClazz();

    public abstract void assertEquals(T model, T response);

    public abstract T instance();

    public abstract T update(T response);

    @Before
    public void before() {
        HttpRequest authRequest = new HttpRequestImpl(RobeAdminTest.getCookie());
        entityClient = new RobeRestClient<>(authRequest, getClazz(), getPath());
    }

    @Test
    public void getAll() throws Exception {
        Response<List<T>> response = entityClient.getAll();
        Assert.assertTrue(response != null);
    }

    @Test
    public void get() throws Exception {
        T model = instance();
        Response<T> create = entityClient.create(model);
        model.setOid(create.getData().getOid());
        assertEquals(model, create.getData());
        Response<T> get = entityClient.get(model.getOid());
        assertEquals(model, get.getData());
        entityClient.delete(model.getOid(), model);
    }

    @Test
    public void create() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        model.setOid(response.getData().getOid());
        assertEquals(model, response.getData());
        entityClient.delete(model.getOid(), model);
    }


    @Test
    public void update() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        T data = response.getData();
        data = update(data);
        response = entityClient.update(data.getOid(), data);
        assertEquals(data, response.getData());
        entityClient.delete(data.getOid(), data);
    }


    @Test
    public void delete() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        model = response.getData();

        entityClient.delete(model.getOid(), model);
        try {
            response = entityClient.get(model.getOid());
            assertTrue("Entity deleted from database. Thats why it can't be exist on database !", false);
        } catch (Exception e) {
            assertTrue("Entity deleted from database.", true);
        }
    }

}
