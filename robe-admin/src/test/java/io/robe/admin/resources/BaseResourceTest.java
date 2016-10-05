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
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Created by hasanmumin on 03/10/16.
 */
public abstract class BaseResourceTest<T extends BaseEntity> extends RobeAdminTest {

    protected RobeRestClient<T, String> entityClient;

    public abstract String getPath();

    public abstract Class<T> getClazz();

    public abstract void assertEquals(T model, T response);

    public abstract void assertEquals(T mergeInstance, T original, T response);

    public abstract T instance();

    public abstract T update(T response);

    public abstract T mergeInstance();

    @Before
    public void before() {
        if (entityClient == null) {
            HttpRequest authRequest = new HttpRequestImpl(RobeAdminTest.getCookie());
            entityClient = new RobeRestClient<>(authRequest, getClazz(), getPath());
        }
    }

    ;

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
    public void updateShouldThrowWebApplicationException1() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        T data = response.getData();
        data = update(data);
        try {
            response = entityClient.update(UUID.randomUUID().toString(), data);
            assertTrue("Sending wrong 'oid', but is not throw exception", false);
        } catch (Exception e) {
            assertTrue("wrong 'oid' throw exception", true);
        }
        entityClient.delete(data.getOid(), data);
    }

    @Test
    public void updateShouldThrowWebApplicationException2() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        T data = response.getData();
        data = update(data);
        String correctOid = data.getOid();
        try {
            String wrongOid = UUID.randomUUID().toString();
            data.setOid(wrongOid);
            response = entityClient.update(data.getOid(), data);
            assertTrue("Sending wrong 'oid', but is not throw exception", false);
        } catch (Exception e) {
            assertTrue("wrong 'oid' throw exception", true);
        }

        data.setOid(correctOid);
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

    @Test
    public void deleteShouldThrowWebApplicationException1() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        model = response.getData();

        try {
            response = entityClient.delete(UUID.randomUUID().toString(), model);
            assertTrue("Entity deleted from database. Thats why it can't be exist on database !", false);
        } catch (Exception e) {
            assertTrue("Entity deleted from database.", true);
            entityClient.delete(model.getOid(), model);
        }
    }

    @Test
    public void deleteShouldThrowWebApplicationException2() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        model = response.getData();

        String correctOid = model.getOid();
        try {

            String wrongOid = UUID.randomUUID().toString();
            model.setOid(wrongOid);
            response = entityClient.delete(model.getOid(), model);
            assertTrue("Entity deleted from database. Thats why it can't be exist on database !", false);
        } catch (Exception e) {
            assertTrue("Entity deleted from database.", true);

            model.setOid(correctOid);
            entityClient.delete(model.getOid(), model);
        }
    }

    @Test
    public void merge() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        T data = response.getData();

        assertEquals(model, data);

        T merge = mergeInstance();
        merge.setOid(data.getOid());
        response = entityClient.merge(merge.getOid(), merge);

        assertEquals(merge, data, response.getData());

        entityClient.delete(data.getOid(), data);
    }

    @Test
    public void mergeShouldThrowWebApplicationException1() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        T data = response.getData();
        assertEquals(model, data);
        T merge = mergeInstance();
        try {
            String wrongOid = UUID.randomUUID().toString();
            merge.setOid(data.getOid());
            response = entityClient.merge(wrongOid, merge);
            assertTrue("Sending wrong 'oid', but is not throw exception", false);
        } catch (Exception e) {
            assertTrue("wrong 'oid' throw exception", true);
            entityClient.delete(data.getOid(), data);
        }
    }

    @Test
    public void mergeShouldThrowWebApplicationException2() throws Exception {
        T model = instance();
        Response<T> response = entityClient.create(model);
        T data = response.getData();

        assertEquals(model, data);

        T merge = mergeInstance();
        try {
            String wrongOid = UUID.randomUUID().toString();
            merge.setOid(wrongOid);
            response = entityClient.merge(merge.getOid(), merge);
            assertTrue("Sending wrong 'oid', but is not throw exception", false);
        } catch (Exception e) {
            assertTrue("wrong 'oid' throw exception", true);
            entityClient.delete(data.getOid(), data);
        }
    }

}
