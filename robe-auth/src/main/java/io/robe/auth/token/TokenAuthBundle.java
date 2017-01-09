package io.robe.auth.token;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.robe.auth.Credentials;
import io.robe.auth.SecurityHeadersFilter;
import io.robe.auth.token.configuration.HasTokenBasedAuthConfiguration;
import io.robe.auth.token.configuration.TokenBasedAuthConfiguration;
import io.robe.auth.token.jersey.TokenBasedAuthResponseFilter;
import io.robe.auth.token.jersey.TokenFactoryProvider;
import io.robe.auth.token.jersey.TokenFeature;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.lang.reflect.Field;

import java.security.NoSuchAlgorithmException;

public class TokenAuthBundle<T extends Configuration & HasTokenBasedAuthConfiguration> implements ConfiguredBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthBundle.class);
    private static final String JCE_SECURITY_CRYPTOGRAPHY_LINK = "http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html";
    private TokenBasedAuthConfiguration configuration;

    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the service's {@link io.dropwizard.setup.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(T configuration, Environment environment) throws Exception {
        this.configuration = configuration.getAuth();
        environment.jersey().register(new TokenFactoryProvider.Binder<Credentials>(Credentials.class));
        environment.jersey().register(new TokenBasedAuthResponseFilter(configuration.getAuth()));
        environment.jersey().register(TokenFeature.class);
        BasicToken.configure(configuration.getAuth());

        if(configuration.getAuth().getAlgorithm() != null) {
            checkCryptography(this.configuration);
        }

        environment.jersey().register(new SecurityHeadersFilter(configuration.getAuth()));
    }

    /**
     * Initializes the service bootstrap.
     *
     * @param bootstrap the service bootstrap
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    /**
     * Determines if cryptography restrictions apply.
     * Restrictions apply if the value of {@link Cipher#getMaxAllowedKeyLength(String)} returns a value smaller than {@link Integer#MAX_VALUE} if there are any restrictions according to the JavaDoc of the method.
     * This method is used with the transform <code>"AES/CBC/PKCS5Padding"</code> as this is an often used algorithm that is <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#impl">an implementation requirement for Java SE</a>.
     *
     * @return <code>true</code> if restrictions apply, <code>false</code> otherwise
     */
    public static void checkCryptography(TokenBasedAuthConfiguration configuration)  {
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (ClassNotFoundException e) {
            StringBuilder builder = new StringBuilder("javax.crypto.JceSecurity class not found !\n" );
            builder.append(getUpgradJceErrorMessage());
            throw new RuntimeException(builder.toString(), e);
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            try {
                if(BasicToken.getEncryptor().isInitialized()) {
                    BasicToken.getEncryptor().encrypt("Sample Data");
                }
            } catch (EncryptionOperationNotPossibleException ex) {
                StringBuilder builder = new StringBuilder();
                builder
                        .append(configuration.getAlgorithm())
                        .append(" not exist on Java Cryptography Extension (JCE)")
                        .append(getUpgradJceErrorMessage());
                LOGGER.error(builder.toString());
                throw new RuntimeException(builder.toString(), ex);
            }
        }
    }

    public static String getUpgradJceErrorMessage() {
        StringBuilder builder = new StringBuilder();
         builder.append("\nYou have to download JCE Security Libraries from ")
                .append(JCE_SECURITY_CRYPTOGRAPHY_LINK)
                .append(" address.")
                .append("\nCopy the Library under of jre/lib/security");
         return  builder.toString();
    }

    public TokenBasedAuthConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(TokenBasedAuthConfiguration configuration) {
        this.configuration = configuration;
    }
}
