package io.robe.admin.hibernate.dao;

import com.google.common.base.Optional;
import javax.inject.Inject;
import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class MailTemplateDao extends BaseDao<MailTemplate> {


    @Inject
    public MailTemplateDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public Optional<MailTemplate> findByCode(String code) {
        Criteria criteria = currentSession().createCriteria(MailTemplate.class);
        criteria.add(Restrictions.eq("code", code));
        return Optional.fromNullable(uniqueResult(criteria));
    }
}
