package io.robe.mail;

import org.junit.Test;

import javax.activation.DataSource;
import java.util.Arrays;
import java.util.HashMap;


public class MailManagerTest {

    @Test
    public void sendMail() throws Exception {
        MailItem item1 = new MailItem("title", "body", (DataSource) null, "sender", "receiver");
        MailItem item2 = new MailItem("title", "body", (DataSource) null, "sender", Arrays.asList("receiver"));
        MailEvent event = new MailEvent() {
            @Override
            public void before(MailItem item) {

            }

            @Override
            public void success(MailItem item) {

            }

            @Override
            public void error(MailItem item, Exception e) {

            }
        };
        item2.setEvent(event);
        assert MailManager.sendMail(item1);
        assert MailManager.sendMail(item2);

        assert !MailManager.hasConfiguration();

        MailConfiguration configuration = new MailConfiguration();
        configuration.setPasswordKey("password");
        configuration.setUsernameKey("username");
        configuration.setProperties(new HashMap<String, Object>());
        MailSender sender = new MailSender(configuration);
        MailManager.setSender(sender);
        assert MailManager.sendMail(item2);
        assert MailManager.sendMail(item1);


    }

}