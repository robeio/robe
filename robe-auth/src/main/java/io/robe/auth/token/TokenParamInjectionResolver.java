package io.robe.auth.token;

import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;

public class TokenParamInjectionResolver extends ParamInjectionResolver {
    public TokenParamInjectionResolver() {
        super(TokenFactoryProvider.class);
    }
}