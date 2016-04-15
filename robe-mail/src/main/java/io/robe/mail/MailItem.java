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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailItem)) return false;

        MailItem item = (MailItem) o;

        if (id != null ? !id.equals(item.id) : item.id != null) return false;
        if (title != null ? !title.equals(item.title) : item.title != null) return false;
        if (body != null ? !body.equals(item.body) : item.body != null) return false;
        if (attachments != null ? !attachments.equals(item.attachments) : item.attachments != null) return false;
        if (sender != null ? !sender.equals(item.sender) : item.sender != null) return false;
        if (receivers != null ? !receivers.equals(item.receivers) : item.receivers != null) return false;
        if (event != null ? !event.equals(item.event) : item.event != null) return false;
        return headers != null ? headers.equals(item.headers) : item.headers == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (receivers != null ? receivers.hashCode() : 0);
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MailItem{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append(", attachments=").append(attachments);
        sb.append(", sender='").append(sender).append('\'');
        sb.append(", receivers=").append(receivers);
        sb.append(", event=").append(event);
        sb.append(", headers=").append(headers);
        sb.append('}');
        return sb.toString();
    }
}
