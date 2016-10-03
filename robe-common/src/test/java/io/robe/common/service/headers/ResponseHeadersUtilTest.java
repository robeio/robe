package io.robe.common.service.headers;

import io.robe.common.TestUtils;
import io.robe.common.service.search.model.SearchModel;
import org.eclipse.jetty.server.Response;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertTrue;

/**
 * Created by hasanmumin on 27/09/16.
 */
public class ResponseHeadersUtilTest {


    private SearchModel model;

    @Before
    public void before() {
        model = new SearchModel();
        model.setResponse(new Response(null, null));
    }


    @Test
    public void constructor() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestUtils.privateConstructor(ResponseHeadersUtil.class);
    }

    @Test
    public void addTotalCount() {
        ResponseHeadersUtil.addTotalCount(model);
        assertTrue(model.getResponse().getHeader("X-Total-Count") != null);
    }

    @Test
    public void addLocation() {
        ResponseHeadersUtil.addLocation(model.getResponse(), "none");
        assertTrue(model.getResponse().getHeader("Location").equals("none"));
    }
}
