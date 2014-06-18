package io.robe.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
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
public class MailSender {

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
     * Sends a mail with the given parameters.
     *
     * @param sender     sender mail address
     * @param receivers  an array of receiver mail addresses
     * @param title      title of mail
     * @param body       body of mail
     * @param attachment a document to attach. If not available send null.
     * @throws MessagingException in case of any problem.
     */
    public void sendMessage(String title, String body, DataSource attachment, String sender, String... receivers) throws MessagingException {
        checkNotNull(receivers);
        checkNotNull(receivers[0]);
        checkNotNull(title);
        checkNotNull(body);

        Message msg = new MimeMessage(session);
        if (sender == null || sender.length() == 0) {
            sender = configuration.getUsername();
        }
        InternetAddress addressFrom = new InternetAddress(sender);
        msg.setFrom(addressFrom);

        MimeBodyPart attachFilePart = new MimeBodyPart();
        if (attachment != null) {
            attachFilePart.setDataHandler(new DataHandler(attachment));
            attachFilePart.setFileName(attachment.getName());
        }

        InternetAddress[] addressTo = new InternetAddress[receivers.length];
        for (int i = 0; i < receivers.length; i++) {
            addressTo[i] = new InternetAddress(receivers[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(title);
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(body, "text/html; charset=UTF-8");
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(textPart);
        if (attachFilePart.getLineCount() > 0) {
            mp.addBodyPart(attachFilePart);
        }

        msg.setContent(mp);
        Transport.send(msg);
        LOGGER.info("Mail sent to :", receivers);
    }


}