package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.SystemParameter;
import org.junit.Assert;

/**
 * Created by hasanmumin on 05/10/2016.
 */
public class SystemParameterResourceTest extends BaseResourceTest<SystemParameter> {
    @Override
    public String getPath() {
        return "systemparameters";
    }

    @Override
    public Class<SystemParameter> getClazz() {
        return SystemParameter.class;
    }

    @Override
    public void assertEquals(SystemParameter model, SystemParameter response) {
        Assert.assertEquals(model.getKey(), response.getKey());
        Assert.assertEquals(model.getValue(), response.getValue());

    }

    @Override
    public SystemParameter instance() {

        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setKey("KEY");
        systemParameter.setValue("VALUE");

        return systemParameter;
    }

    @Override
    public SystemParameter update(SystemParameter response) {

        response.setKey("KEY-1");
        response.setValue("VALUE-1");

        return response;
    }
}
