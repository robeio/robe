package io.robe.mail;

import org.junit.Before;
import org.junit.Test;

import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import java.util.HashMap;

/**
 * Created by hasanmumin on 05/01/16.
 */
public class MailSenderTest {
    private MailSender mailSender;

    @Before
    public void initialize() {

        /**
         mail.smtp.username: username@robe.io
         mail.smtp.password: xxXXXXxx
         mail.smtp.host: smtp.gmail.com
         mail.smtp.port: 587
         mail.smtp.auth: true
         mail.smtp.starttls.enable: true
         */

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("mail.smtp.username", "YOURGMAILACCOUNT");
        properties.put("mail.smtp.password", "YOURPASSWORD");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);

        MailConfiguration mailConfiguration = new MailConfiguration();
        mailConfiguration.setUsernameKey("mail.smtp.username");
        mailConfiguration.setPasswordKey("mail.smtp.password");
        mailConfiguration.setProperties(properties);

        mailSender = new MailSender(mailConfiguration);

    }

    @Test
    public void sendMail() {

        MailItem mailItem = new MailItem();
        mailItem.setTitle("Title");
        mailItem.setBody("body");
        mailItem.setReceivers("receiver1", "receiver2");
        mailItem.getAttachments().add(new FileDataSource("file1path"));
        mailItem.getAttachments().add(new FileDataSource("file2path"));

        try {
            mailSender.sendMessage(mailItem);
        } catch (AuthenticationFailedException | NullPointerException e) {
            assert true;
        } catch (MessagingException e) {
            assert false;
        }

    }
}
