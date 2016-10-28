package io.robe.common.service.search.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class SearchModelTest {
    @Test
    public void setFilterExpression() throws Exception {
        SearchModel model = new SearchModel();
        model.setFilterExpression("field=1,field!=1,field>=1,field>1,field<=1,field<1,field~=1,field|=1|2|3");
        String[][]expected = new String[][]{
                new String[]{"field","=","1"},
                new String[]{"field","!=","1"},
                new String[]{"field",">=","1"},
                new String[]{"field",">","1"},
                new String[]{"field","<=","1"},
                new String[]{"field","<","1"},
                new String[]{"field","~=","1"},
                new String[]{"field","|=","1|2|3"},
        };
        assertEquals(expected.length,model.getFilter().length);
        assertArrayEquals(expected,model.getFilter());
    }

    @Test
    public void addFilter() throws Exception {

    }

}