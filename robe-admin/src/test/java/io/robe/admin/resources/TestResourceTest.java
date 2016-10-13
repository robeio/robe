package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.Language;
import org.junit.Assert;

/**
 * Created by hasanmumin on 13/10/2016.
 */
public class TestResourceTest extends BaseResourceTest<Language> {
    @Override
    public String getPath() {
        return "tests";
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
        language.setName("TEST");
        language.setCode(Language.Type.EN);
        return language;
    }

    @Override
    public Language update(Language response) {
        response.setName("TEST-1");
        return response;
    }

    @Override
    public Language mergeInstance() {
        Language language = new Language();
        language.setName("TEST-2");
        return language;
    }
}
