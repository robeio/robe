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
        Response<List<T>> response = this.getAllFrom();
        Assert.assertTrue(response != null);// TODO change
    }

    @Test
    public void get() throws Exception {
        T data = this.createFrom().getData();
        this.assertEquals(instance(), data);
        Response<T> get = this.getFrom(data.getOid());
        this.assertEquals(data, get.getData());
        this.deleteFrom(data);
    }

    @Test
    public void create() throws Exception {
        T data = this.createFrom().getData();
        this.assertEquals(instance(), data);
        this.deleteFrom(data);
    }


    @Test
    public void update() throws Exception {
        T data = this.createFrom().getData();
        this.assertEquals(instance(), data);
        data = this.update(data);
        Response<T> response = this.updateFrom(data);
        this.assertEquals(data, response.getData());
        this.deleteFrom(data);
    }

    @Test
    public void updateShouldThrowWebApplicationException1() throws Exception {
        T data = this.createFrom().getData();
        this.assertEquals(instance(), data);
        data = this.update(data);
        try {
            this.updateFrom(this.randomUUID(), data);
            assertTrue("Sending wrong 'oid', but is not throw exception", false);
        } catch (Exception e) {
            assertTrue("wrong 'oid' throw exception", true);
        }
        this.deleteFrom(data);
    }

    @Test
    public void updateShouldThrowWebApplicationException2() throws Exception {
        T data = this.createFrom().getData();
        this.assertEquals(instance(), data);
        data = this.update(data);
        String correctOid = data.getOid();
        try {
            data.setOid(this.randomUUID());
            this.updateFrom(data);
            assertTrue("Sending wrong 'oid', but is not throw exception", false);
        } catch (Exception e) {
            assertTrue("wrong 'oid' throw exception", true);
        }

        data.setOid(correctOid);
        this.deleteFrom(data);
    }


    @Test
    public void delete() throws Exception {
        T data = this.createFrom().getData();
        this.deleteFrom(data);
        try {
            this.getFrom(data.getOid());
            assertTrue("Entity deleted from database. Thats why it can't be exist on database !", false);
        } catch (Exception e) {
            assertTrue("Entity deleted from database.", true);
        }
    }

    @Test
    public void deleteShouldThrowWebApplicationException1() throws Exception {
        T data = this.createFrom().getData();
        try {
            this.deleteFrom(this.randomUUID(), data);
            assertTrue("Entity deleted from database. Thats why it can't be exist on database !", false);
        } catch (Exception e) {
            assertTrue("Entity deleted from database.", true);
            this.deleteFrom(data);
        }
    }

    @Test
    public void deleteShouldThrowWebApplicationException2() throws Exception {
        T data = this.createFrom().getData();
        this.assertEquals(instance(), data);
        String correctOid = data.getOid();
        try {
            data.setOid(this.randomUUID());
            this.deleteFrom(data);
            assertTrue("Entity deleted from database. Thats why it can't be exist on database !", false);
        } catch (Exception e) {
            assertTrue("Entity deleted from database.", true);
        }

        data.setOid(correctOid);
        this.deleteFrom(data);
    }

    @Test
    public void merge() throws Exception {
        T data = this.createFrom().getData();
        this.assertEquals(instance(), data);
        T merge = this.mergeInstance();
        merge.setOid(data.getOid());
        Response<T> response = this.mergeFrom(merge);
        this.assertEquals(merge, data, response.getData());
        this.deleteFrom(data);
    }

    @Test
    public void mergeShouldThrowWebApplicationException1() throws Exception {
        T data = this.createFrom().getData();
        this.assertEquals(instance(), data);
        T merge = mergeInstance();
        try {
            merge.setOid(data.getOid());
            this.mergeFrom(this.randomUUID(), merge);
            assertTrue("Sending wrong 'oid', but is not throw exception", false);
        } catch (Exception e) {
            assertTrue("wrong 'oid' throw exception", true);
        }
        this.deleteFrom(data);
    }

    @Test
    public void mergeShouldThrowWebApplicationException2() throws Exception {
        T data = createFrom().getData();
        assertEquals(instance(), data);
        T merge = mergeInstance();
        try {
            merge.setOid(this.randomUUID());
            this.mergeFrom(merge);
            assertTrue("Sending wrong 'oid', but is not throw exception", false);
        } catch (Exception e) {
            assertTrue("wrong 'oid' throw exception", true);
        }
        this.deleteFrom(data);
    }

    protected String randomUUID() {
        return UUID.randomUUID().toString();
    }

    protected Response<T> createFrom(T model) throws Exception {
        return entityClient.create(model);
    }

    protected Response<T> createFrom() throws Exception {
        return createFrom(instance());
    }

    protected Response<T> deleteFrom(T model) throws Exception {
        return deleteFrom(model.getOid(), model);
    }

    protected Response<T> deleteFrom(String oid, T model) throws Exception {
        return entityClient.delete(oid, model);
    }

    protected Response<T> updateFrom(T model) throws Exception {
        return updateFrom(model.getOid(), model);
    }

    protected Response<T> updateFrom(String oid, T model) throws Exception {
        return entityClient.update(oid, model);
    }

    protected Response<T> mergeFrom(T model) throws Exception {
        return mergeFrom(model.getOid(), model);
    }

    protected Response<T> mergeFrom(String oid, T model) throws Exception {
        return entityClient.merge(oid, model);
    }

    protected Response<T> getFrom(String oid) throws Exception {
        return entityClient.get(oid);
    }

    protected Response<List<T>> getAllFrom() throws Exception {
        return entityClient.getAll();
    }
}
