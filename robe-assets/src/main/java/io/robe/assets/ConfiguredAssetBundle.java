package io.robe.assets;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.servlets.assets.AssetServlet;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;

/**
 * A Configured bundle for serving static asset files from the asset system.
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
        for (AssetConfiguration assetConf : configuration.getAssets()) {
            String resourcePath = assetConf.getResourcePath();
            Preconditions.checkArgument(resourcePath.startsWith("/"), "%s is not an absolute path", resourcePath);
            Preconditions.checkArgument(!"/".equals(resourcePath), "%s is the classpath root", resourcePath);
            resourcePath = resourcePath.endsWith("/") ? resourcePath : (resourcePath + '/');
            String uriPath = assetConf.getUriPath();
            uriPath = uriPath.endsWith("/") ? uriPath : (uriPath + '/');
            LOGGER.info("Registering AssetBundle with name: {} for path {}", assetConf.getAssetsName(), uriPath + '*');
            HttpServlet assetServlet = null;
            switch (assetConf.getType()) {
                case "filesystem":
                    assetServlet = getFileAssetServlet(assetConf, resourcePath, uriPath);
                    break;
                case "classpath":
                    assetServlet = getClasspathAssetServlet(assetConf,resourcePath,uriPath);
                    break;
            }
            environment.servlets().addServlet(assetConf.getAssetsName(), assetServlet).addMapping(uriPath + '*');
        }
    }

    private HttpServlet getClasspathAssetServlet(AssetConfiguration assetConf, String resourcePath, String uriPath) {
        return new AssetServlet(resourcePath,uriPath,assetConf.getIndexFile(), Charsets.UTF_8);
    }

    private FileAssetServlet getFileAssetServlet(AssetConfiguration conf, String resourcePath, String uriPath) {
        return new FileAssetServlet(
                resourcePath,
                uriPath,
                conf.getIndexFile(),
                Charsets.UTF_8,
                conf.getCached());
    }

    @Override
    public void initialize(Bootstrap bootstrap) {

    }

}
