package io.robe.auth.tokenbased.filter;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import com.yammer.dropwizard.auth.Auth;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by serayuzgur on 18/03/14.
 */
public class TokenBasedAuthResourceFilterFactory implements ResourceFilterFactory{

    private final TokenBasedAuthConfiguration configuration;
    private final List<ResourceFilter> filterList;

    public TokenBasedAuthResourceFilterFactory(TokenBasedAuthConfiguration configuration){
        this.configuration = configuration;
        filterList = new LinkedList();
        filterList.add(new TokenBasedAuthResourceFilter(configuration));
    }

    /**
     * Create a list of {@link com.sun.jersey.spi.container.ResourceFilter} instance given a method
     * of the abstract resource model.
     * <p/>
     * When applying the list of resource filters to a request each resource filter
     * is applied, in order, from the first to last entry in the list.
     * When applying the list of resource filters to a response each resource filter
     * is applied, in reverse order, from the last to first entry in the list.
     *
     * @param am the abstract method. This may be an instance
     *           of the following: {@link AbstractResourceMethod},
     *           {@link AbstractSubResourceMethod} or {@link AbstractSubResourceLocator}.
     * @return the list of resource filter, otherwise an empty list or null if
     * no resource filters are associated with the method.
     */
    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        for(Annotation[] annotations  :am.getMethod().getParameterAnnotations()){
            for(Annotation annotation : annotations){
                if (annotation instanceof Auth){
                    return filterList;
                }
            }
        }
        return null;
    }
}
