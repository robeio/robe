package io.robe.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import io.robe.guice.GuiceConfiguration;
import io.robe.hibernate.HibernateConfiguration;
import io.robe.mail.MailConfiguration;
import io.robe.mq.MessageQueueConfiguration;
import io.robe.quartz.QuartzConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RobeServiceConfiguration extends Configuration {

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


    public HibernateConfiguration getHibernateConfiguration() {
        return hibernate;
    }

    public MailConfiguration getMailConfiguration() {
        return mail;
    }

    public QuartzConfiguration getQuartzConfiguration() {
        return quartz;
    }

    public GuiceConfiguration getGuiceConfiguration() {
        return guice;
    }

    public MessageQueueConfiguration getMqConfiguration() {
        return messageQueue;
    }
}
