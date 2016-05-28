package io.robe.admin.hibernate.dao;

import com.google.common.base.Optional;
import javax.inject.Inject;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import java.util.List;

public class SystemParameterDao extends BaseDao<SystemParameter> {

    @Inject
    public SystemParameterDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public Optional<SystemParameter> findByKey(String key) {
        Criteria criteria = currentSession().createCriteria(SystemParameter.class);
        criteria.add(Restrictions.eq("key", key));
        return Optional.fromNullable(uniqueResult(criteria));
    }

    public List getAll() {
        Criteria criteria = currentSession().createCriteria(SystemParameter.class);

        criteria.setProjection(
                Projections.distinct(Projections.projectionList()
                        .add(Projections.property("key"), "key").add(Projections.property("value"), "value")));

        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return list(criteria);
    }

}
