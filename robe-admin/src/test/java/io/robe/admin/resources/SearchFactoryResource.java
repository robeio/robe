package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.LanguageDao;
import io.robe.admin.hibernate.entity.Language;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by hasanmumin on 19/10/2016.
 */

@Path("searches")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SearchFactoryResource {
    @Inject
    private LanguageDao languageDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public List<Language> getAll(@SearchParam SearchModel search) {
        return languageDao.findAll(search);
    }
}
