package io.robe.as2;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class As2Bundle<T extends Configuration & HasAs2Configuration> implements ConfiguredBundle<T> {

	@Override
	public void run(T t, Environment environment) throws Exception {
		As2Configuration conf = t.getAs2Configuration();
		environment.getApplicationContext().addServlet(As2Servlet.class, conf.getPath());
	}

	@Override
	public void initialize(Bootstrap<?> bootstrap) {

	}
}
