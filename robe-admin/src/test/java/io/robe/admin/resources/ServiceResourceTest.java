package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.data.entry.ServiceEntry;
import io.robe.test.request.TestRequest;
import io.robe.test.request.TestResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by hasanmumin on 11/10/2016.
 */
public class ServiceResourceTest extends BaseResourceTest<Service> {
    @Override
    public String getPath() {
        return "services";
    }

    @Override
    public Class<Service> getClazz() {
        return Service.class;
    }

    @Override
    public void assertEquals(Service model, Service response) {
        Assert.assertEquals(model.getDescription(), response.getDescription());
        Assert.assertEquals(model.getGroup(), response.getGroup());
        Assert.assertEquals(model.getPath(), response.getPath());
    }

    @Override
    public void assertEquals(Service mergeInstance, Service original, Service response) {
        Assert.assertEquals(mergeInstance.getDescription(), response.getDescription());
        Assert.assertEquals(original.getGroup(), response.getGroup());
        Assert.assertEquals(original.getPath(), response.getPath());
    }

    @Override
    public Service instance() {
        Service service = new Service();
        service.setDescription("Description");
        service.setGroup("GROUP");
        service.setMethod(ServiceEntry.Method.GET);
        service.setPath("path");

        return service;
    }

    @Override
    public Service update(Service response) {
        response.setDescription("Description updated");
        return response;
    }

    @Override
    public Service mergeInstance() {

        Service service = new Service();
        service.setDescription("escription updated again");
        return service;
    }

    @Test
    public void refresh() throws IOException {
        TestRequest request = getRequestBuilder().endpoint("refresh").build();
        TestResponse response = client.get(request);
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void groups() throws IOException {
        TestRequest request = getRequestBuilder().endpoint("groups").build();
        TestResponse response = client.get(request);
        Assert.assertEquals(response.getStatus(), 200);
        Assert.assertTrue(response.list(getClazz()).size() > 0);
    }

}
