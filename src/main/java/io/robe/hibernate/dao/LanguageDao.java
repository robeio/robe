package io.robe.hibernate.dao;

import com.google.inject.Inject;
import io.robe.hibernate.entity.Language;
import org.hibernate.SessionFactory;

/**
 * Created by kaanalkim on 13/02/14.
 */
public class LanguageDao extends BaseDao<Language> {

    @Inject
    public LanguageDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
