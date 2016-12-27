package io.robe.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.robe.admin.recaptcha.ReCaptchaConfiguration;
import io.robe.assets.AssetConfiguration;
import io.robe.assets.HasAssetConfiguration;
import io.robe.auth.token.configuration.HasTokenBasedAuthConfiguration;
import io.robe.auth.token.configuration.TokenBasedAuthConfiguration;
import io.robe.guice.GuiceConfiguration;
import io.robe.guice.HasGuiceConfiguration;
import io.robe.hibernate.HasHibernateConfiguration;
import io.robe.hibernate.HibernateConfiguration;
import io.robe.mail.HasMailConfiguration;
import io.robe.mail.MailConfiguration;
import io.robe.quartz.configuration.HasQuartzConfiguration;
import io.robe.quartz.configuration.QuartzConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RobeConfiguration extends Configuration implements
        HasHibernateConfiguration,
        HasGuiceConfiguration,
        HasQuartzConfiguration,
        HasMailConfiguration,
        HasTokenBasedAuthConfiguration,
        HasAssetConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private HibernateConfiguration hibernate;

    @Valid
    @JsonProperty
    private MailConfiguration mail;

    @Valid
    @NotNull
    @JsonProperty
    private QuartzConfiguration quartz;

    @Valid
    @JsonProperty
    private GuiceConfiguration guice;

    @Valid
    @JsonProperty
    private TokenBasedAuthConfiguration auth;

    @Valid
    @JsonProperty
    private List<AssetConfiguration> assets;

    @Valid
    @JsonProperty
    private ReCaptchaConfiguration recaptcha;


    public HibernateConfiguration getHibernate() {
        return hibernate;
    }

    @Override
    public MailConfiguration getMail() {
        return mail;
    }

    @Override
    public QuartzConfiguration getQuartz() {
        return quartz;
    }

    @Override
    public GuiceConfiguration getGuice() {
        return guice;
    }


    @Override
    public TokenBasedAuthConfiguration getAuth() {
        return auth;
    }

    @Override
    public List<AssetConfiguration> getAssets() {
        return assets;
    }

    public ReCaptchaConfiguration getRecaptcha() {
        return recaptcha;
    }
}
