package io.robe.as2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class As2Servlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(As2Servlet.class);
	private static final String GET_MESSAGE = "Get Methods are not supported for now";
	private static final long serialVersionUID = -2850794040708785318L;

	private transient String serviceName;


	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		this.serviceName = getParam(config.getInitParameter("service-name"), null);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
		resp.setContentType("text/plain");
		final PrintWriter writer = resp.getWriter();
		try {
			writer.println(GET_MESSAGE);
		} finally {
			writer.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//TODO: AS2 connector will do the rest. Log for testing.
		LOGGER.info("POST received.");
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
		resp.setContentType("text/plain");
		final PrintWriter writer = resp.getWriter();
		try {
			writer.println(GET_MESSAGE);
		} finally {
			writer.close();
		}
	}


	private static String getParam(String initParam, String defaultValue) {
		return initParam == null ? defaultValue : initParam;
	}

}
