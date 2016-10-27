package io.robe.hibernate.dao;

import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.entity.RobeEntity;
import io.robe.hibernate.entity.TestEntity;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BaseDaoTest {
    private SearchModel getModel() {
        SearchModel expected = new SearchModel();
        expected.setQ("abc");
        expected.setOffset(1);
        expected.setLimit(10);
        expected.setFields(new String[]{"name", "time", "count"});
        expected.setSort(new String[]{"+day", "-time"});
        expected.setFilter("status=ACTIVE");
        expected.setResponse(mock(HttpServletResponse.class));
        return expected;
    }

    private final SessionFactory factory = mock(SessionFactory.class);
    private final Criteria criteria = mock(Criteria.class);
    private final Session session = mock(Session.class);
//    private final Projection projection = mock(Projection.class);

    @Before
    public void setup() throws Exception {
        when(factory.getCurrentSession()).thenReturn(session);
        when(session.createCriteria(RobeEntity.class)).thenReturn(criteria);
        when(criteria.setProjection(anyObject())).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(1l);
    }

    @Test
    public void findAll() throws Exception {
        BaseDao<TestEntity> baseDao = new BaseDao<>(factory);
        baseDao.findAll(getModel());
    }

    @Test
    public void findAllWithSearchFrom() throws Exception {

    }

}