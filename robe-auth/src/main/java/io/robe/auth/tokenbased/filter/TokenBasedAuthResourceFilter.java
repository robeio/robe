package io.robe.auth.tokenbased.filter;

import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;

public class TokenBasedAuthResourceFilter implements ResourceFilter {

    private final TokenBasedAuthResponseFilter responseFilter;

    public TokenBasedAuthResourceFilter(TokenBasedAuthConfiguration configuration){
        responseFilter = new TokenBasedAuthResponseFilter(configuration);
    }
    /**
     * Get the request filter.
     *
     * @return the request filter, otherwise null if request filtering
     * is not supported.
     */
    @Override
    public ContainerRequestFilter getRequestFilter() {
        return null;
    }

    /**
     * Get the response filter.
     *
     * @return the response filter, otherwise null if response filtering
     * is not supported.
     */
    @Override
    public ContainerResponseFilter getResponseFilter() {
        return responseFilter;
    }
}
