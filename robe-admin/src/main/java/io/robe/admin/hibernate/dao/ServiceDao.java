package io.robe.admin.hibernate.dao;

import com.google.common.base.Optional;
import javax.inject.Inject;
import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.data.store.ServiceStore;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import java.util.List;

public class ServiceDao extends BaseDao<Service> implements ServiceStore {

    @Inject
    public ServiceDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Service> findByCode(String code) {
        return Optional.fromNullable(findById(code));
    }

    public Service findByPathAndMethod(String path, Service.Method method) {
        Criteria criteria = currentSession().createCriteria(Service.class);
        criteria.add(Restrictions.eq("path", path));
        criteria.add(Restrictions.eq("method", method));
        return uniqueResult(criteria);
    }


    public List<Service> findServiceByGroups() {
        Criteria criteria = currentSession().createCriteria(Service.class);
        criteria.setProjection(
                Projections.projectionList()
                        .add(Projections.property("group"), "group")
                        .add(Projections.groupProperty("group")))
                .setResultTransformer(Transformers.aliasToBean(Service.class));

        return list(criteria);
    }

    public List<Service> findServiceByGroup(String group) {
        Criteria criteria = currentSession().createCriteria(Service.class);
        criteria.add(Restrictions.eq("group", group));
        return list(criteria);
    }
}
