package io.robe.admin.resources;

import io.robe.admin.RobeAdminTest;
import io.robe.admin.util.request.HttpClient;
import io.robe.admin.util.request.TestRequest;
import io.robe.admin.util.request.TestResponse;
import io.robe.hibernate.entity.BaseEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Created by hasanmumin on 03/10/16.
 */
public abstract class BaseResourceTest<T extends BaseEntity> extends RobeAdminTest {

    public static final String BASE_URL = "http://127.0.0.1:8080/robe/";

    protected final HttpClient client = HttpClient.getClient();

    protected final TestRequest.Builder requestBuilder = new TestRequest.Builder(BASE_URL + getPath());

    public abstract String getPath();

    public abstract Class<T> getClazz();

    public abstract void assertEquals(T model, T response);

    public abstract void assertEquals(T mergeInstance, T original, T response);

    public abstract T instance();

    public abstract T update(T response);

    public abstract T mergeInstance();


    @Test
    public void getAll() throws Exception {
        List<T> response = this.getAllFrom();
        Assert.assertTrue(response != null);// TODO change
    }

    @Test
    public void get() throws Exception {
        T data = this.createFrom();
        this.assertEquals(instance(), data);
        T get = this.getFrom(data.getOid());
        this.assertEquals(data, get);
        this.deleteFrom(data);
    }

    @Test
    public void create() throws Exception {
        T data = this.createFrom();
        this.assertEquals(instance(), data);
        this.deleteFrom(data);
    }


    @Test
    public void update() throws Exception {
        T data = this.createFrom();
        this.assertEquals(instance(), data);
        data = this.update(data);
        T response = this.updateFrom(data);
        this.assertEquals(data, response);
        this.deleteFrom(data);
    }

    @Test
    public void updateShouldThrowWebApplicationException1() throws Exception {
        T data = this.createFrom();
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
        T data = this.createFrom();
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
        T data = this.createFrom();
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
        T data = this.createFrom();
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
        T data = this.createFrom();
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
        T data = this.createFrom();
        this.assertEquals(instance(), data);
        T merge = this.mergeInstance();
        merge.setOid(data.getOid());
        T response = this.mergeFrom(merge);
        this.assertEquals(merge, data, response);
        this.deleteFrom(data);
    }

    @Test
    public void mergeShouldThrowWebApplicationException1() throws Exception {
        T data = this.createFrom();
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
        T data = createFrom();
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

    protected T createFrom(T model) throws Exception {
        TestResponse response = client.post(requestBuilder.entity(model).build());
        return response.get(getClazz());
    }

    protected T createFrom() throws Exception {
        return createFrom(instance());
    }

    protected T deleteFrom(T model) throws Exception {
        return deleteFrom(model.getOid(), model);
    }

    protected T deleteFrom(String oid, T model) throws Exception {
        TestResponse response = client.delete(requestBuilder.endpoint(oid).entity(model).build());
        return response.get(getClazz());
    }

    protected T updateFrom(T model) throws Exception {
        return updateFrom(model.getOid(), model);
    }

    protected T updateFrom(String oid, T model) throws Exception {
        TestResponse response = client.put(requestBuilder.endpoint(oid).entity(model).build());
        return response.get(getClazz());
    }

    protected T mergeFrom(T model) throws Exception {
        return mergeFrom(model.getOid(), model);
    }

    protected T mergeFrom(String oid, T model) throws Exception {
        TestResponse response = client.patch(requestBuilder.endpoint(oid).entity(model).build());
        return response.get(getClazz());
    }

    protected T getFrom(String oid) throws Exception {
        TestResponse response = client.get(requestBuilder.endpoint(oid).build());
        return response.get(getClazz());
    }

    protected List<T> getAllFrom() throws Exception {
        TestResponse response = client.get(requestBuilder.build());
        return response.list(getClazz());
    }
}
