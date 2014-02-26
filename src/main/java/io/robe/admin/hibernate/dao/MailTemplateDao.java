package io.robe.admin.hibernate.dao;

import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.SessionFactory;

/**
 * Created by kaanalkim on 11/02/14.
 */
public class MailTemplateDao extends BaseDao<MailTemplate> {


    @Inject
    public MailTemplateDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
