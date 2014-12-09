package io.robe.assets;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Configured bundle for serving static asset files from the file system.
 */
public class ConfiguredAssetBundle<T extends Configuration & HasAssetConfiguration> implements ConfiguredBundle<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguredAssetBundle.class);


	/**
	 * Creates a new ConfiguredAssetBundle which will configure the application to serve the static files
	 */
	public ConfiguredAssetBundle() {

	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		for(AssetConfiguration assetConf : configuration.getAssets()) {
			String resourcePath = assetConf.getResourcePath();
			Preconditions.checkArgument(resourcePath.startsWith("/"), "%s is not an absolute path", resourcePath);
			Preconditions.checkArgument(!"/".equals(resourcePath), "%s is the classpath root", resourcePath);
			resourcePath = resourcePath.endsWith("/") ? resourcePath : (resourcePath + '/');
			String uriPath = assetConf.getUriPath();
			uriPath = uriPath.endsWith("/") ? uriPath : (uriPath + '/');
			LOGGER.info("Registering AssetBundle with name: {} for path {}", assetConf.getAssetsName(), uriPath + '*');
			environment.servlets().addServlet(assetConf.getAssetsName(),
					new FileAssetServlet(
							resourcePath,
							uriPath,
							assetConf.getIndexFile(),
							Charsets.UTF_8)
			).addMapping(uriPath + '*');
		}
	}

	@Override
	public void initialize(Bootstrap bootstrap) {

	}

}
