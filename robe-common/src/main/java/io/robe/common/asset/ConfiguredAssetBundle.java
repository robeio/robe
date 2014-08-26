package io.robe.common.asset;

import com.google.common.base.Charsets;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A Configured bundle for serving static asset files from the file system.
 */
public class ConfiguredAssetBundle<T extends Configuration & HasAssetConfiguration> implements ConfiguredBundle<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguredAssetBundle.class);


	/**
	 * Creates a new AssetsBundle which will configure the application to serve the static files
	 * located in {@code src/main/resources/${resourcePath}} as {@code /${uriPath}}. If no file name is
	 * in ${uriPath}, ${indexFile} is appended before serving. For example, given a
	 * {@code resourcePath} of {@code "/assets"} and a uriPath of {@code "/js"},
	 * {@code src/main/resources/assets/example.js} would be served up from {@code /js/example.js}.
	 */
	public ConfiguredAssetBundle() {

	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		String resourcePath = configuration.getAsset().getResourcePath();
		checkArgument(resourcePath.startsWith("/"), "%s is not an absolute path", resourcePath);
		checkArgument(!"/".equals(resourcePath), "%s is the classpath root", resourcePath);
		resourcePath = resourcePath.endsWith("/") ? resourcePath : (resourcePath + '/');
		String uriPath = configuration.getAsset().getUriPath();
		uriPath = uriPath.endsWith("/") ? uriPath : (uriPath + '/');
		LOGGER.info("Registering AssetBundle with name: {} for path {}", configuration.getAsset().getAssetsName(), uriPath + '*');
		environment.servlets().addServlet(configuration.getAsset().getAssetsName(),
				new FileAssetServlet(
						resourcePath,
						uriPath,
						configuration.getAsset().getIndexFile(),
						Charsets.UTF_8)
		).addMapping(uriPath + '*');
	}

	@Override
	public void initialize(Bootstrap bootstrap) {

	}

}
