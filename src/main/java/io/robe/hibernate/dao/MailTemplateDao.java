package io.robe.hibernate.dao;

import com.google.inject.Inject;
import io.robe.hibernate.entity.MailTemplate;
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
