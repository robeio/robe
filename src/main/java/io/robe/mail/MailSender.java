package io.robe.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


public class MailSender {

    private static MailSender INSTANCE;

    protected static void createInstance(MailProfile profile) {
        INSTANCE = new MailSender(profile);
    }

    public static MailSender getInstance() {
        return INSTANCE;
    }

    private final MailProfile profile;

    private MailSender(MailProfile profile) {
        this.profile = profile;
    }

    public void sendMessage(String sender, String[] receivers, String title, String body, DataSource attachment) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", profile.getHost());
        props.put("mail.smtp.port", profile.getPort());
        props.put("mail.smtp.auth", profile.isAuth());
        //props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", profile.isTlsssl());
        //props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        // props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(profile.getUsername(), profile.getPassword());
            }
        });

        Message msg = new MimeMessage(session);
        if (sender == null || sender.length() == 0)
            sender = profile.getUsername();

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