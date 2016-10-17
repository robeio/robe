package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.admin.util.request.TestRequest;
import io.robe.admin.util.request.TestResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
    public void assertEquals(SystemParameter mergeInstance, SystemParameter original, SystemParameter response) {
        Assert.assertEquals(response.getKey(), response.getKey());
        Assert.assertEquals(mergeInstance.getValue(), response.getValue());
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

    @Override
    public SystemParameter mergeInstance() {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setValue("VALUE-2");
        return systemParameter;
    }

    @Test
    public void bulkSaveOrUpdate() throws Exception {

        SystemParameter parameter = super.createFrom();

        Map<String, String> values = new HashMap<>();
        values.put("KEY", "VALUE_CHANGED");
        values.put("KEY2", "VALUE-2");
        TestRequest request = getRequestBuilder().endpoint("admin").entity(values).build();
        TestResponse response = client.post(request);
        Map result = response.get(Map.class);
        Assert.assertTrue(result.size() == 2);

        super.deleteFrom(parameter);
    }
}
