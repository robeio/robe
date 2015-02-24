package io.robe.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A mail sender class.
 */
class MailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);
    private static final Properties PROPERTIES = new Properties();
    private final MailConfiguration configuration;
    private Session session;

    /**
     * @param configuration mail configuration
     */
    public MailSender(MailConfiguration configuration) {
        checkNotNull(configuration);
        this.configuration = configuration;
        setProperties();
    }

    private void setProperties() {
        LOGGER.debug("Setting configuration.");
        LOGGER.debug(configuration.toString());
        PROPERTIES.put("mail.smtp.host", configuration.getHost());
        PROPERTIES.put("mail.smtp.port", configuration.getPort());
        PROPERTIES.put("mail.smtp.auth", configuration.isAuth());
        PROPERTIES.put("mail.smtp.starttls.enable", configuration.isTlsssl());
        session = Session.getDefaultInstance(PROPERTIES, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configuration.getUsername(), configuration.getPassword());
            }
        });
    }


    /**
     * Sends a mail with the given item.
     *
     * @param item MailItem to be send.
     */
    public void sendMessage(MailItem item) throws MessagingException {
        checkNotNull(item.getReceivers());
        checkNotNull(item.getReceivers().get(0));
        checkNotNull(item.getTitle());
        checkNotNull(item.getBody());

        //If sender is empty send with the account sender.
        Message msg = new MimeMessage(session);
        if (item.getSender() == null || item.getSender().length() == 0) {
            item.setSender(configuration.getUsername());
        }
        InternetAddress from = new InternetAddress(item.getSender());
        msg.setFrom(from);

        MimeBodyPart attachFilePart = new MimeBodyPart();
        if (item.getAttachment() != null) {
            attachFilePart.setDataHandler(new DataHandler(item.getAttachment()));
            attachFilePart.setFileName(item.getAttachment().getName());
        }

        InternetAddress[] to = new InternetAddress[item.getReceivers().size()];
        for (int i = 0; i < item.getReceivers().size(); i++) {
            to[i] = new InternetAddress(item.getReceivers().get(i));
        }
        msg.setRecipients(Message.RecipientType.TO, to);

        msg.setSubject(item.getTitle());

        MimeBodyPart body = new MimeBodyPart();
        body.setContent(item.getBody(), "text/html; charset=UTF-8");
        Multipart content = new MimeMultipart();
        content.addBodyPart(body);
        if (item.getAttachment() != null) {
            content.addBodyPart(attachFilePart);
        }

        msg.setContent(content);
        Transport.send(msg);
    }


}