package io.robe.common.service.search;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by adem on 21/10/2016.
 */
public class SearchFactoryTest {

    class QueryParamValueFactoryProvider extends AbstractContainerRequestValueFactory {
        @Override
        public Object provide() {
            return null;
        }
    }

    //@Test
    public void tay() {
        ContainerRequest request = mock(ContainerRequest.class);
        AbstractContainerRequestValueFactory valueFactory = mock(AbstractContainerRequestValueFactory.class);
        //when(valueFactory.getContainerRequest()).thenCallRealMethod();
    }

}
