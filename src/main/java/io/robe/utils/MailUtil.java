package io.robe.utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


public class MailUtil {

    private static final String SMTP_HOST_NAME = "mail.gvg.com.tr";
    private static final String SMTP_PORT = "587";
    private static final String MAINSENDER = "software@gvg.com.tr";//"web@gvg.com.tr";//"idrissenocak@gmail.com";


    public static void sendMessage(String sender, String[] receivers, String title, String body, DataSource attachment) throws MessagingException {
        boolean debug = true;

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        //props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        //props.put("mail.smtp.starttls.enable","true");
        //props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MAINSENDER, "!guanyin2013GVG!");
                    }
                });

        session.setDebug(debug);

        Message msg = new MimeMessage(session);
        if (sender == null || sender.length() == 0)
            sender = MAINSENDER;

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


//	public static void sendMessage(String[] receivers ,MailTemplate template ,DataSource attachment) throws MessagingException {
//		sendMessage(template.getSender(),receivers, template.getTitle(), template.getBody(), attachment);
//	}


}