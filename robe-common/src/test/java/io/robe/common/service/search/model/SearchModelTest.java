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

    @Test
    public void hashCodeTest() {

        SearchModel model1 = new SearchModel();
        SearchModel model2 = new SearchModel();
        this.check(model1, model2);

        String[] fields = new String[]{"field1", "field2"};
        model1.setFields(fields);
        model2.setFields(fields);
        this.check(model1, model2);

        Integer offset = 0;
        model1.setOffset(offset);
        model2.setOffset(offset);
        this.check(model1, model2);

        Integer limit = 10;
        model1.setLimit(limit);
        model2.setLimit(limit);
        this.check(model1, model2);

        String q = "query";
        model1.setQ(q);
        model2.setQ(q);
        this.check(model1, model2);

        String[] sort = new String[]{"field1+", "field2-"};
        model1.setSort(sort);
        model2.setSort(sort);
        this.check(model1, model2);

        long totalCount = 30;
        model1.setTotalCount(totalCount);
        model2.setTotalCount(totalCount);
        this.check(model1, model2);

        HttpServletResponse httpServletResponse = new Response(null, null);
        model1.setResponse(httpServletResponse);
        model2.setResponse(httpServletResponse);
        this.check(model1, model2);

        String[][] filter = new String[][]{new String[]{"field1", "=", "val"}};
        model1.setFilter(filter);
        model2.setFilter(filter);


    }

    public void check(SearchModel model1, SearchModel model2) {
        assertEquals(model1.hashCode(), model2.hashCode());
        assertEquals(model1, model2);
    }

}