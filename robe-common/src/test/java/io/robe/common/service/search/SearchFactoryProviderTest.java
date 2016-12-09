package io.robe.common.service.search;

import io.robe.common.service.search.model.SearchModel;
import org.junit.Test;
import org.glassfish.jersey.server.model.Parameter;
import org.mockito.Mockito;

import static org.junit.Assert.*;


public class SearchFactoryProviderTest {

    private Parameter getParameter(String methodName, Class clazz) throws NoSuchMethodException {
        return Parameter.create(
                clazz,
                SampleResource.class,
                SampleResource.class.getDeclaredMethod(methodName, clazz),
                true
        ).get(0);
    }

    @Test
    public void createValueFactory() throws Exception {
        Parameter parameter = getParameter("test1", SearchModel.class);
        SearchFactoryProvider provider = Mockito.mock(SearchFactoryProvider.class);
        Mockito.when(provider.createValueFactory(parameter)).thenCallRealMethod();
        SearchFactory factory = (SearchFactory) provider.createValueFactory(parameter);
        assertNotNull(factory);
    }

    @Test
    public void createValueFactoryWithoutAnnotation() throws Exception {
        Parameter parameter = getParameter("test2", SearchModel.class);
        SearchFactoryProvider provider = Mockito.mock(SearchFactoryProvider.class);
        Mockito.when(provider.createValueFactory(parameter)).thenCallRealMethod();
        SearchFactory factory = (SearchFactory) provider.createValueFactory(parameter);
        assertNull(factory);
    }

    @Test
    public void createValueFactoryWrongType() throws Exception {
        Parameter parameter = getParameter("test3", String.class);
        SearchFactoryProvider provider = Mockito.mock(SearchFactoryProvider.class);
        Mockito.when(provider.createValueFactory(parameter)).thenCallRealMethod();
        SearchFactory factory = (SearchFactory) provider.createValueFactory(parameter);
        assertNull(factory);
    }

    private class SampleResource {
        public String test1(@SearchParam SearchModel model) {
            return "OK";
        }

        public String test2(SearchModel model) {
            return "OK";
        }

        public String test3(@SearchParam String text) {
            return "OK";
        }
    }

}