package io.robe.common.service.search;

import io.robe.common.service.search.model.SearchModel;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;


public class SearchFactory extends AbstractContainerRequestValueFactory<SearchModel> {

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletResponse response;

    public SearchFactory() {
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    /**
     * implemented just to GET method.
     *
     * @return SearchModel
     */
    @Override
    public SearchModel provide() {

        SearchModel searchModel = new SearchModel();

        searchModel.setResponse(response);

        String method = getMethod();

        if ("GET".equals(method)) {

            MultivaluedMap<String, String> queryParameters = getUriInfo().getQueryParameters();

            for (Map.Entry<String, List<String>> param : queryParameters.entrySet()) {

                if (param.getValue().get(0) == null)
                    continue;
                if ("_q".equalsIgnoreCase(param.getKey())) {
                    searchModel.setQ(param.getValue().get(0));
                } else if ("_limit".equalsIgnoreCase(param.getKey())) {
                    searchModel.setLimit(Integer.parseInt(param.getValue().get(0)));
                } else if ("_offset".equalsIgnoreCase(param.getKey())) {
                    searchModel.setOffset(Integer.parseInt(param.getValue().get(0)));
                } else if ("_fields".equalsIgnoreCase(param.getKey())) {
                    searchModel.setFields(param.getValue().get(0).split(","));
                } else if ("_sort".equalsIgnoreCase(param.getKey())) {
                    searchModel.setSort(param.getValue().get(0).split(","));
                } else if ("_filter".equalsIgnoreCase(param.getKey())) {
                    searchModel.setFilterExpression(param.getValue().get(0));
                }
            }
        }
        return searchModel;
    }

    public String getMethod() {
        return getContainerRequest().getMethod();
    }
}
