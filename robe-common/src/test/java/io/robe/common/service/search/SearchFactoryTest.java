package io.robe.common.service.search;

import com.google.common.collect.Lists;
import io.robe.common.service.search.model.SearchModel;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class SearchFactoryTest {


    @Test
    public void provide() throws Exception {
        SearchModel expected = new SearchModel();
        expected.setQ("qparam");
        expected.setOffset(1);
        expected.setLimit(10);
        expected.setFields(new String[]{"field1", "field2"});
        expected.setSort(new String[]{"+field1", "-field2"});
        expected.setFilterExpression("field1=1");

        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.put("_q", Lists.newArrayList("qparam"));
        queryParameters.put("_offset", Lists.newArrayList("1"));
        queryParameters.put("_limit", Lists.newArrayList("10"));
        queryParameters.put("_fields", Lists.newArrayList("field1,field2"));
        queryParameters.put("_sort", Lists.newArrayList("+field1,-field2"));
        queryParameters.put("_filter", Lists.newArrayList("field1=1"));

        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
        SearchFactory factory = mock(SearchFactory.class);
        when(factory.getUriInfo()).thenReturn(uriInfo);
        when(factory.getMethod()).thenReturn("GET");
        when(factory.provide()).thenCallRealMethod();
        SearchModel actual = factory.provide();

        assertEquals(expected, actual);

    }

}