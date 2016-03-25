package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.LanguageDao;
import io.robe.admin.hibernate.entity.Language;
import io.robe.auth.Credentials;
import io.robe.common.service.SearchParam;
import io.robe.common.service.jersey.model.SearchModel;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("languages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LanguageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageResource.class);

    @Inject
    private LanguageDao languageDao;

    /**
     * @param credentials auto fill by @{@link Auth} annotation.
     * @param searchModel auto fill by @{@link SearchParam} annotation.
     *                    example  _q=tr&_limit=5&_offset=0&_fields=name,code
     * @return list of {@link Language}
     */

    // TODO io.robe.hibernate.dao.BaseDao not implemented yet for SearchModel.
    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public List<Language> getAll(@Auth Credentials credentials, @SearchParam SearchModel searchModel) {
        return languageDao.findAll(Language.class);
    }
}
