package io.robe.audit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.spi.dispatch.RequestDispatcher;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class AuditedMethodRequestDispatcher implements RequestDispatcher {
	private final static Set<String> REDACTED_HEADERS = ImmutableSet.of(HttpHeaders.AUTHORIZATION);
	private final RequestDispatcher dispatcher;
	private final Logger LOGGER;

	public AuditedMethodRequestDispatcher(RequestDispatcher dispatcher, Logger LOGGER) {
		this.dispatcher = Preconditions.checkNotNull(dispatcher);
		this.LOGGER = Preconditions.checkNotNull(LOGGER);
	}

	@Override
	public void dispatch(Object resource, HttpContext context) {
		logRequest(resource, context);
		dispatcher.dispatch(resource, context);
	}

	private void logRequest(Object resource, HttpContext context) {
		final HttpRequestContext request = context.getRequest();
		final StringBuilder builder = new StringBuilder();
		builder.append("\nPROTECTED RESOURCE ACCESS\n");
		builder.append("  Resource : ").append(resource.getClass()).append("\n");


		for (Map.Entry<String, List<String>> entry : request.getRequestHeaders().entrySet()) {
			if (!REDACTED_HEADERS.contains(entry.getKey())) {
				builder.append("  Header   : ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
			}
		}
		builder.append("  Method   : ").append(request.getMethod()).append("\n");
		builder.append("  URI      : ").append(request.getRequestUri()).append("\n");
		for (Map.Entry<String, List<String>> entry : request.getQueryParameters(true).entrySet()) {
			final String name = entry.getKey();
			final List<String> value = entry.getValue();
			builder.append("  Param    : ").append(name).append(" = ").append(value).append(" \n");
		}
		LOGGER.info(builder.toString());
	}


}