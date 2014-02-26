package io.robe.audit;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceMethodDispatchAdapter;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;

import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;


/**
 * Dispatcher method for {@link Audited} annotation
 */
public class AuditedMethodDispatchProvider implements ResourceMethodDispatchProvider {
	private final ResourceMethodDispatchProvider provider;
    private static final Logger LOGGER = Logger.getLogger(AuditedMethodDispatchProvider.class.getName());


    public AuditedMethodDispatchProvider(ResourceMethodDispatchProvider provider) {
		this.provider = Preconditions.checkNotNull(provider);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param abstractResourceMethod
	 * @return
	 */
	@Override
	public RequestDispatcher create(AbstractResourceMethod abstractResourceMethod) {
		final RequestDispatcher dispatcher = provider.create(abstractResourceMethod);
		final Audited audited = abstractResourceMethod.getAnnotation(Audited.class);
		if (audited != null) {
			return new AuditedMethodRequestDispatcher(dispatcher, LOGGER);
		}
		return dispatcher;
	}


	/**
	 * {@inheritDoc}
	 */
	@Provider
	public static class AuditedMethodDispatchAdapter implements ResourceMethodDispatchAdapter {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public ResourceMethodDispatchProvider adapt(ResourceMethodDispatchProvider provider) {
			return new AuditedMethodDispatchProvider(provider);
		}

	}


}