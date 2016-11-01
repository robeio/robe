package io.robe.common.service.search.model;

import org.eclipse.jetty.server.Response;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class SearchModelTest {
    @Test
    public void setFilterExpression() throws Exception {
        SearchModel model = new SearchModel();
        model.setFilterExpression("field=1,field!=1,field>=1,field>1,field<=1,field<1,field~=1,field|=1|2|3");
        String[][] expected = new String[][]{
                new String[]{"field", "=", "1"},
                new String[]{"field", "!=", "1"},
                new String[]{"field", ">=", "1"},
                new String[]{"field", ">", "1"},
                new String[]{"field", "<=", "1"},
                new String[]{"field", "<", "1"},
                new String[]{"field", "~=", "1"},
                new String[]{"field", "|=", "1|2|3"},
        };
        assertEquals(expected.length, model.getFilter().length);
        assertArrayEquals(expected, model.getFilter());
    }

    @Test
    public void getterSetter() {
        SearchModel model = new SearchModel();

        String[] fields = new String[]{"field1", "field2"};
        model.setFields(fields);
        assertArrayEquals(fields, model.getFields());

        Integer offset = 0;
        model.setOffset(offset);
        assertEquals(offset, model.getOffset());

        Integer limit = 10;
        model.setLimit(limit);
        assertEquals(limit, model.getLimit());

        String q = "query";
        model.setQ(q);
        assertEquals(q, model.getQ());

        String[] sort = new String[]{"field1+", "field2-"};
        model.setSort(sort);
        assertArrayEquals(sort, model.getSort());

        long totalCount = 30;
        model.setTotalCount(totalCount);
        assertEquals(totalCount, model.getTotalCount(), 0);

        HttpServletResponse httpServletResponse = new Response(null, null);

        model.setResponse(httpServletResponse);
        assertEquals(httpServletResponse, model.getResponse());

        String[][] filter = new String[][]{new String[]{"field1", "=", "val"}};
        model.setFilter(filter);
        assertArrayEquals(filter, model.getFilter());

    }

    @Test
    public void addFilter() throws Exception {
        SearchModel model = new SearchModel();

        model.addFilter("field1", "=", "val");
        assertArrayEquals(new String[][]{new String[]{"field1", "=", "val"}}, model.getFilter());

        model.addFilter("field2", "<", "5");
        assertArrayEquals(new String[][]{new String[]{"field1", "=", "val"}, new String[]{"field2", "<", "5"}}, model.getFilter());
    }

    @Test
    public void addSort() {

        SearchModel model = new SearchModel();

        model.addSort("field", "-");
        assertArrayEquals(new String[]{"-field"}, model.getSort());

        model.addSort("field1", "+");
        assertArrayEquals(new String[]{"-field", "+field1"}, model.getSort());

        model.addSort("field", "+");
        assertArrayEquals(new String[]{"+field", "+field1"}, model.getSort());
    }

}