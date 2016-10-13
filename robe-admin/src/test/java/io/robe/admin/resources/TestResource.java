package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.Language;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by hasanmumin on 13/10/2016.
 */
@Path("tests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestResource extends BaseResource<Language> {
}
