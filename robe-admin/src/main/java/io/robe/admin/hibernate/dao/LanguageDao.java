package io.robe.admin.hibernate.dao;

import javax.inject.Inject;
import io.robe.admin.hibernate.entity.Language;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.SessionFactory;

public class LanguageDao extends BaseDao<Language> {

    @Inject
    public LanguageDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
