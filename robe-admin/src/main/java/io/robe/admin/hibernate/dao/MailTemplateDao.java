package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.Optional;

public class MailTemplateDao extends BaseDao<MailTemplate> {


    @Inject
    public MailTemplateDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public Optional<MailTemplate> findByCode(String code) {
        Criteria criteria = currentSession().createCriteria(MailTemplate.class);
        criteria.add(Restrictions.eq("code", code));
        return Optional.ofNullable(uniqueResult(criteria));
    }
}
