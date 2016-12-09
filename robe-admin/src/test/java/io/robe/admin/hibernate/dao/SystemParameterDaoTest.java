package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.SystemParameter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class SystemParameterDaoTest extends BaseDaoTest<SystemParameter, SystemParameterDao> {
    @Override
    public SystemParameter instance() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setValue("VALUE");
        systemParameter.setKey("KEY");
        return systemParameter;
    }

    @Override
    public SystemParameter update(SystemParameter model) {
        model.setValue("VALUE_CHANGED");
        return model;
    }

    @Test
    public void findByKey() {
        super.createFrom();
        Optional<SystemParameter> systemParameter = dao.findByKey("KEY");
        Assert.assertTrue(systemParameter.isPresent());
        super.deleteFrom(systemParameter.get());
    }
}
