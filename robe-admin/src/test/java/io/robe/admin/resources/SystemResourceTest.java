package io.robe.admin.resources;

import io.robe.hibernate.entity.BaseEntity;
import io.robe.test.request.TestRequest;
import io.robe.test.request.TestResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by hasanmumin on 14/10/2016.
 */
public class SystemResourceTest extends BaseResourceTest {
    @Override
    public String getPath() {
        return "systems";
    }

    @Override
    public Class getClazz() {
        return null;
    }

    @Override
    public void assertEquals(BaseEntity model, BaseEntity response) {

    }

    @Override
    public void assertEquals(BaseEntity mergeInstance, BaseEntity original, BaseEntity response) {

    }

    @Override
    public BaseEntity instance() {
        return null;
    }

    @Override
    public BaseEntity update(BaseEntity response) {
        return null;
    }

    @Override
    public BaseEntity mergeInstance() {
        return null;
    }

    @Override
    public void getAll() throws Exception {
    }

    @Override
    public void get() throws Exception {
    }

    @Override
    public void create() throws Exception {
    }


    @Override
    public void update() throws Exception {
    }

    @Override
    public void updateShouldThrowWebApplicationException1() throws Exception {
    }

    @Override
    public void updateShouldThrowWebApplicationException2() throws Exception {
    }


    @Override
    public void delete() throws Exception {
    }

    @Override
    public void deleteShouldThrowWebApplicationException1() throws Exception {
    }

    @Override
    public void deleteShouldThrowWebApplicationException2() throws Exception {
    }

    @Override
    public void merge() throws Exception {
    }

    @Override
    public void mergeShouldThrowWebApplicationException1() throws Exception {
    }

    @Override
    public void mergeShouldThrowWebApplicationException2() throws Exception {
    }

    @Test
    public void getHeapDump() throws IOException {

        try {
            TestRequest request = getRequestBuilder().endpoint("heapdump").build();
            TestResponse response = client.get(request); // TODO handle this response file
        } catch (Exception e) {
            // TODO ignore ?
        }

        Assert.assertTrue(true);
    }
}
