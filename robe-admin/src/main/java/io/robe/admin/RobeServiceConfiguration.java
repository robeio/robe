package io.robe.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import io.robe.auth.tokenbased.configuration.HasTokenBasedAuthConfiguration;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import io.robe.guice.GuiceConfiguration;
import io.robe.guice.HasGuiceConfiguration;
import io.robe.hibernate.HasHibernateConfiguration;
import io.robe.hibernate.HibernateConfiguration;
import io.robe.mail.HasMailConfiguration;
import io.robe.mail.MailConfiguration;
import io.robe.mq.HasMessageQueueConfiguration;
import io.robe.mq.MessageQueueConfiguration;
import io.robe.quartz.configuration.HasQuartzConfiguration;
import io.robe.quartz.configuration.QuartzConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RobeServiceConfiguration extends Configuration implements
        HasHibernateConfiguration,
        HasGuiceConfiguration,
        HasQuartzConfiguration,
        HasMailConfiguration,
        HasMessageQueueConfiguration,
        HasTokenBasedAuthConfiguration {

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
    private MessageQueueConfiguration messageQueue;

    @Valid
    @JsonProperty
    private TokenBasedAuthConfiguration auth;


    public HibernateConfiguration getHibernateConfiguration() {
        return hibernate;
    }

    @Override
    public MailConfiguration getMailConfiguration() {
        return mail;
    }

    @Override
    public QuartzConfiguration getQuartzConfiguration() {
        return quartz;
    }

    @Override
    public GuiceConfiguration getGuiceConfiguration() {
        return guice;
    }

    @Override
    public MessageQueueConfiguration getMessageQueueConfiguration() {
        return messageQueue;
    }

    @Override
    public TokenBasedAuthConfiguration getTokenBasedAuthConfiguration() {
        return auth;
    }
}
