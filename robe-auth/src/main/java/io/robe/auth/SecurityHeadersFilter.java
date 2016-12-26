package io.robe.auth;

import io.robe.auth.token.configuration.TokenBasedAuthConfiguration;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;

public class SecurityHeadersFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private final TokenBasedAuthConfiguration config;

    public SecurityHeadersFilter(TokenBasedAuthConfiguration config) {
        this.config = config;
    }


    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        // if token is secure and connection is un-secure redirect to https
        if (!request.getSecurityContext().isSecure() && config.isSecure()) {
            URI location = UriBuilder.fromUri(request.getUriInfo().getRequestUri()).scheme("https").build();
            throw new WebApplicationException(Response.status(Response.Status.MOVED_PERMANENTLY).location(location).build());
        }
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        //Add HSTS header if protocol is https
        if (request.getSecurityContext().isSecure()) {
            MultivaluedMap<String, Object> headers = response.getHeaders();
            headers.putSingle("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        }

        response.getHeaders().putSingle("X-Frame-Options", "deny");
        response.getHeaders().putSingle("X-XSS-Protection", "1; mode=block");
    }
}