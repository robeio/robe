package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.data.entry.ServiceEntry;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class ServiceDaoTest extends BaseDaoTest<Service, ServiceDao> {
    @Override
    public Service instance() {
        Service service = new Service();
        service.setDescription("Description of Service");
        service.setPath("robe");
        service.setMethod(ServiceEntry.Method.GET);
        service.setGroup("ROBE");
        return service;
    }

    @Override
    public Service update(Service model) {
        model.setDescription("Description of Service Updated");
        return model;
    }

    @Test
    public void findByPathAndMethod() {
        super.createFrom();
        Service service = dao.findByPathAndMethod("robe", ServiceEntry.Method.GET);
        Assert.assertTrue(service != null);
        Assert.assertEquals(service.getPath(), "robe");
        Assert.assertEquals(service.getGroup(), "ROBE");
        super.deleteFrom(service);
    }

    @Test
    public void findServiceByGroups() {
        List<Service> services = dao.findServiceByGroups();
        Assert.assertTrue(services.size() > 0); // TODO
    }

    @Test
    public void findServiceByGroup() {
        super.createFrom();
        List<Service> services = dao.findServiceByGroup("ROBE");
        Assert.assertTrue(services.size() == 1);
        Service service = services.get(0);
        Assert.assertEquals(service.getGroup(), "ROBE");
        super.deleteFrom(service);
    }
}
