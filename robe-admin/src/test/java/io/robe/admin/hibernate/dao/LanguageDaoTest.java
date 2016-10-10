package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hasanmumin on 07/10/2016.
 */
public class LanguageDaoTest extends BaseDaoTest<Language, LanguageDao> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageDaoTest.class);

    @Override
    public Language instance() {
        Language language = new Language();
        language.setCode(Language.Type.EN);
        language.setName("NAME");
        return language;
    }

    @Override
    public Language update(Language model) {
        model.setName("NAME-1");
        return model;
    }
}
