package io.robe.mail;

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
 * A singleton mail sender class.
 */
public class MailSender {

    private static MailSender INSTANCE;

    /**
     * Creates an instance for singleton. Will be called from {@link io.robe.mail.MailBundle}
     *
     * @param configuration mail configuration
     */
    protected static void createInstance(MailConfiguration configuration) {
        INSTANCE = new MailSender(configuration);
    }

    /**
     * Returns an instance of mail sender
     * @return
     */
    public static MailSender getInstance() {
        return INSTANCE;
    }

    private final MailConfiguration configuration;

    /**
     * @param configuration mail configuration
     */
    private MailSender(MailConfiguration configuration) {
        this.configuration = configuration;
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
    public void sendMessage(String sender, String[] receivers, String title, String body, DataSource attachment) throws MessagingException {

        checkNotNull(sender);
        checkNotNull(receivers);
        checkNotNull(receivers[0]);
        checkNotNull(title);
        checkNotNull(body);
        Properties props = new Properties();
        props.put("mail.smtp.host", configuration.getHost());
        props.put("mail.smtp.port", configuration.getPort());
        props.put("mail.smtp.auth", configuration.isAuth());
        props.put("mail.smtp.starttls.enable", configuration.isTlsssl());

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configuration.getUsername(), configuration.getPassword());
            }
        });

        Message msg = new MimeMessage(session);
        if (sender == null || sender.length() == 0)
            sender = configuration.getUsername();

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
        if (attachFilePart.getLineCount() > 0)
            mp.addBodyPart(attachFilePart);

        msg.setContent(mp);
        Transport.send(msg);
    }


}