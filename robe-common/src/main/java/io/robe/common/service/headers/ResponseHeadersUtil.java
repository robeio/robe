package io.robe.common.service.headers;

import io.robe.common.service.search.model.SearchModel;

import javax.servlet.http.HttpServletResponse;

public final class ResponseHeadersUtil {
    private ResponseHeadersUtil() {

    }

    public static final void addTotalCount(SearchModel model) {
        model.getResponse().setHeader("X-Total-Count", model.getTotalCount() + "");
    }

    public static final void addLocation(HttpServletResponse response, String location) {
        response.setHeader("Location", location);
    }
}
