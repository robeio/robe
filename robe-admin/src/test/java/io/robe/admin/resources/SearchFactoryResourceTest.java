package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.Language;
import io.robe.common.service.search.model.SearchModel;
import io.robe.test.request.TestRequest;
import io.robe.test.request.TestResponse;
import org.junit.Test;

/**
 * Created by hasanmumin on 19/10/2016.
 */
public class SearchFactoryResourceTest extends BaseResourceTest<Language> {
    @Override
    public String getPath() {
        return "searches";
    }

    @Override
    public Class<Language> getClazz() {
        return Language.class;
    }

    @Override
    public void assertEquals(Language model, Language response) {

    }

    @Override
    public void assertEquals(Language mergeInstance, Language original, Language response) {

    }

    @Override
    public Language instance() {
        Language language = new Language();
        language.setName("NAME");
        language.setCode(Language.Type.EN);
        return language;
    }

    @Override
    public Language update(Language response) {
        return null;
    }

    @Override
    public Language mergeInstance() {
        return null;
    }


    @Override
    public void get() throws Exception {
    }

    @Override
    public void create() throws Exception {
    }


    @Override
    public void update() throws Exception {
    }

    @Override
    public void updateShouldThrowWebApplicationException1() throws Exception {
    }

    @Override
    public void updateShouldThrowWebApplicationException2() throws Exception {
    }


    @Override
    public void delete() throws Exception {
    }

    @Override
    public void deleteShouldThrowWebApplicationException1() throws Exception {
    }

    @Override
    public void deleteShouldThrowWebApplicationException2() throws Exception {
    }

    @Override
    public void merge() throws Exception {
    }

    @Override
    public void mergeShouldThrowWebApplicationException1() throws Exception {
    }

    @Override
    public void mergeShouldThrowWebApplicationException2() throws Exception {
    }

    @Override
    public void getAll() throws Exception {
    }

    @Test
    public void search() throws Exception {

        SearchModel model = new SearchModel();
        model.setFields(new String[]{"name", "code"});
        model.addFilter("name", "=", "NAME");
        model.addSort("name", "-");
        model.setLimit(10);
        model.setOffset(0);
        model.setQ("NAME");

        TestRequest request = getRequestBuilder().search(model).build();

        TestResponse response = client.get(request);

        Object result = response.get(Object.class);

        assert result != null;

    }
}
