package io.robe.mail;

import javax.activation.DataSource;
import java.util.*;

/**
 * MailItem is for holding mail entries. It supports standard mail attributes with  one attachments and an {@link io.robe.mail.MailEvent} for executing before and after
 */
public class MailItem {
    /**
     * An automatic ID. It can be changed later with a different one.
     */
    private String id = System.currentTimeMillis() + "";
    private String title;
    private String body;
    private List<DataSource> attachments = new ArrayList<>();
    private String sender;
    private List<String> receivers;
    private MailEvent event;
    private Map<String, String[]> headers = new HashMap<>();

    public MailItem() {
    }

    public MailItem(String title, String body, DataSource attachments, String sender, String... receivers) {
        this.title = title;
        this.body = body;
        this.attachments = Collections.singletonList(attachments);
        this.sender = sender;
        this.receivers = Arrays.asList(receivers);
    }

    public MailItem(String title, String body, DataSource attachments, String sender, List<String> receivers) {
        this.title = title;
        this.body = body;
        this.attachments = Collections.singletonList(attachments);
        this.sender = sender;
        this.receivers = receivers;
    }

    public MailItem(String title, String body, List<DataSource> attachments, String sender, String... receivers) {
        this.title = title;
        this.body = body;
        this.attachments = attachments;
        this.sender = sender;
        this.receivers = Arrays.asList(receivers);
    }

    public MailItem(String title, String body, List<DataSource> attachments, String sender, List<String> receivers) {
        this.title = title;
        this.body = body;
        this.attachments = attachments;
        this.sender = sender;
        this.receivers = receivers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {

        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<DataSource> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<DataSource> attachments) {
        this.attachments = attachments;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(String... receivers) {
        this.receivers = Arrays.asList(receivers);
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public MailEvent getEvent() {
        return event;
    }

    public void setEvent(MailEvent event) {
        this.event = event;
    }

    public Map<String, String[]> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String[]> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "MailItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", attachments=" + attachments +
                ", sender='" + sender + '\'' +
                ", receivers=" + receivers +
                ", event=" + event +
                ", headers=" + headers +
                '}';
    }
}
