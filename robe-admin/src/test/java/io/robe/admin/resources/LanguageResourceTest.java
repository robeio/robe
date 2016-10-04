package io.robe.admin.resources;


import io.robe.admin.RobeAdminTest;
import io.robe.admin.hibernate.entity.Language;
import io.robe.admin.rest.http.HttpRequest;
import io.robe.admin.rest.Response;
import io.robe.admin.rest.RobeRestClient;
import io.robe.admin.rest.http.HttpRequestImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by kamilbukum on 27/09/16.
 */

public class LanguageResourceTest extends RobeAdminTest {

    static RobeRestClient<Language, String> entityClient;

    @BeforeClass
    public static void before(){
        HttpRequest authRequest = new HttpRequestImpl(RobeAdminTest.getCookie());
        entityClient = new RobeRestClient(authRequest, Language.class, "languages");
    }

    @Test
    public void getAll() throws Exception {
        Response<List<Language>> response = entityClient.getAll();
        assertEquals(response.getData().size(), 2);
    }


    @Test
    public void get() throws Exception {
        Response<List<Language>> response = entityClient.getAll();
        for(Language language: response.getData()) {
            Response<Language> langResponse = entityClient.get(language.getOid());
            assertEquals(language.getCode(), langResponse.getData().getCode());
            assertEquals(language.getName(), langResponse.getData().getName());
        }
    }

    @Test
    public void create() throws Exception {
        Language model = new Language();
        model.setCode(Language.Type.EN);
        model.setName("tr");
        Response<Language> response = entityClient.create(model);
        assertEquals(response.getData().getName(), model.getName());
        assertEquals(response.getData().getCode(), model.getCode());
        entityClient.delete(response.getData().getOid(), response.getData());
    }

    @Test
    public void update() throws Exception {
        Language model = new Language();
        model.setCode(Language.Type.EN);
        model.setName("tr");
        Response<Language> response = entityClient.create(model);
        model = response.getData();
        model.setCode(Language.Type.TR);
        model.setName("fr");
        response = entityClient.update(model.getOid(), model);
        assertEquals(response.getData().getCode(),model.getCode());
        assertEquals(response.getData().getName(), model.getName());
    }

    @Test
    public void merge() throws Exception {
        Language model = new Language();
        model.setCode(Language.Type.EN);
        model.setName("tr");
        Response<Language> response = entityClient.create(model);
        response = entityClient.merge(response.getData().getOid(), model);
        assertEquals(response.getData().getCode(),model.getCode());
        assertEquals(response.getData().getName(), model.getName());
    }

    @Test
    public void delete() throws Exception {
        Language model = new Language();
        model.setCode(Language.Type.EN);
        model.setName("tr");
        Response<Language> response = entityClient.create(model);
        model = response.getData();
        model.setCode(Language.Type.TR);
        model.setName("fr");
        entityClient.delete(model.getOid(), model);
        try {
            response = entityClient.get(model.getOid());
            assertTrue("Entity deleted from database. Thats why it can't be exist on database ! ", false);
        }catch (Exception e){
            assertTrue("Entity deleted from database.", true);
        }
    }


}