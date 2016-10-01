package io.robe.common.service.search.model;

import io.robe.common.service.search.SearchableEnum;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class SearchableEnumTest {
    SearchableEnum searchableEnum;

    @Before
    public void setUp() throws Exception {
        searchableEnum = EasyMock.createMock(SearchableEnum.class);
    }

    @Test
    public void test() throws Exception {
        EasyMock.expect(searchableEnum.getText()).andReturn("test");
        EasyMock.replay(searchableEnum);
        assertEquals("test", searchableEnum.getText());
    }
}
