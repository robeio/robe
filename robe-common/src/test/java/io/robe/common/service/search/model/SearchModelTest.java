package io.robe.common.service.search.model;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by recep on 02/10/16.
 */
@FixMethodOrder
public class SearchModelTest {
    SearchModel model;
    String[] fields;
    String[] sort;

    @Before
    public void setUp() throws Exception {
        model = new SearchModel();
        fields = new String[]{"a", "b", "c"};
        sort = new String[]{"a", "b", "c", "d"};
        model.setFields(fields);
        model.setSort(sort);

    }

    @Test
    public void getQ() throws Exception {
        model.setQ("q");
        assertEquals("q", model.getQ());
    }

    @Test
    public void getOffset() throws Exception {
        model.setOffset(2);
        assertEquals(2, (int) model.getOffset());
    }

    @Test
    public void getLimit() throws Exception {
        model.setLimit(3);
        assertEquals(3, (int) model.getLimit());
    }

    @Test
    public void getFields() throws Exception {
        assertEquals(fields, model.getFields());
    }

    @Test
    public void getSort() throws Exception {
        assertEquals(sort, model.getSort());
    }

    @Test
    public void getFilter() throws Exception {
        model.setFilter("filter");
        assertEquals("filter", model.getFilter());
    }

    @Test
    public void getTotalCount() throws Exception {
        model.setTotalCount(3);
        assertEquals(3, model.getTotalCount());
    }


    @Test
    public void getResponse() throws Exception {
        model.setResponse(null);
        assertNull(model.getResponse());
    }

    @Test
    public void addFilter() throws Exception {
        model.setFilter(null);
        model.addFilter("a", "=", "b");
        assertEquals("a=b", model.getFilter());

        model.setFilter("a=1");
        model.addFilter("a", "=", "1");
        assertEquals("a=1", model.getFilter());

        model.setFilter("a=1");
        model.addFilter("b", "=", "2");
        assertEquals("a=1,b=2", model.getFilter());

    }

    @Test
    public void addSort() throws Exception {
        model.addSort("e", "-");
        assertNotEquals(sort, model.getSort());

        model.addSort("a", "-");
        assertNotEquals(sort, model.getSort());

        model.setSort(null);
        model.addSort("a", "-");
        assertNotEquals(sort, model.getSort());


    }
}
