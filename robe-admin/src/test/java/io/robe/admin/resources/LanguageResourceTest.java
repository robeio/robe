package io.robe.admin.resources;


import io.robe.admin.hibernate.entity.Language;
import org.junit.Assert;

/**
 * Created by kamilbukum on 27/09/16.
 */

public class LanguageResourceTest extends BaseResourceTest<Language> {

    @Override
    public String getPath() {
        return "languages";
    }

    @Override
    public Class<Language> getClazz() {
        return Language.class;
    }

    @Override
    public void assertEquals(Language model, Language response) {
        Assert.assertEquals(model.getName(), response.getName());
        Assert.assertEquals(model.getCode(), response.getCode());
    }

    @Override
    public void assertEquals(Language mergeInstance, Language original, Language response) {
        Assert.assertEquals(mergeInstance.getName(), response.getName());
        Assert.assertEquals(original.getCode(), response.getCode());

    }

    @Override
    public Language instance() {
        Language language = new Language();
        language.setCode(Language.Type.EN);
        language.setName("LANG_NAME");
        return language;
    }

    @Override
    public Language update(Language response) {
        response.setName("LANG_NAME_1");
        return response;
    }

    @Override
    public Language mergeInstance() {

        Language language = new Language();
        language.setName("NEW_NAME");

        return language;
    }
}