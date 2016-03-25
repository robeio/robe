package io.robe.common.service.jersey;

import io.robe.common.service.SearchParam;
import io.robe.common.service.jersey.model.SearchModel;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class SearchFactoryProvider extends AbstractValueFactoryProvider {

    @Inject
    public SearchFactoryProvider(final MultivaluedParameterExtractorProvider extractorProvider,
                                 ServiceLocator locator) {

        super(extractorProvider, locator, Parameter.Source.UNKNOWN);
    }

    @Override
    protected Factory<?> createValueFactory(Parameter parameter) {
        Class<?> paramType = parameter.getRawType();
        SearchParam annotation = parameter.getAnnotation(SearchParam.class);
        if (annotation != null && paramType.isAssignableFrom(SearchModel.class)) {
            return new SearchFactory();
        }
        return null;
    }

    private static class SearchParamInjectionResolver extends ParamInjectionResolver {
        public SearchParamInjectionResolver() {
            super(SearchFactoryProvider.class);
        }
    }

    public static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(SearchFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
            bind(SearchParamInjectionResolver.class).to(new TypeLiteral<InjectionResolver<SearchParam>>() {
            }).in(Singleton.class);
        }
    }
}
