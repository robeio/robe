package io.robe.admin.hibernate.dao;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.Menu;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MenuDao extends BaseDao<Menu> {

    @Inject
    public MenuDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Menu> findByCode(String code) {
        Criteria criteria = currentSession().createCriteria(Menu.class);
        criteria.add(Restrictions.eq("code", code));
        return Optional.fromNullable(uniqueResult(criteria));
    }

    public List<Menu> findHierarchicalMenu() {
        Criteria criteria = currentSession().createCriteria(Menu.class);
        criteria.add(Restrictions.isNull("parentOid"));
        criteria.addOrder(Order.desc("itemOrder"));
        return list(criteria);
    }
}
