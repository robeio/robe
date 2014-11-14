package io.robe.mail;

import javax.activation.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * MailItem is for holding mail entries. It supports standard mail attributes with  one attachment and an {@link io.robe.mail.MailEvent} for executing before and after
 */
public class MailItem {
	/**
	 * An automatic ID. It can be changed later with a different one.
	 */
	private String id = System.currentTimeMillis() + "";
	private String title;
	private String body;
	private DataSource attachment;
	private String sender;
	private List<String> receivers;
	private MailEvent event;

	public MailItem() {
	}

	public MailItem(String title, String body, DataSource attachment, String sender, String... receivers) {
		this.title = title;
		this.body = body;
		this.attachment = attachment;
		this.sender = sender;
		this.receivers = Arrays.asList(receivers);
	}

	public MailItem(String title, String body, DataSource attachment, String sender, List<String> receivers) {
		this.title = title;
		this.body = body;
		this.attachment = attachment;
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

	public DataSource getAttachment() {
		return attachment;
	}

	public void setAttachment(DataSource attachment) {
		this.attachment = attachment;
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

	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}

	public void setReceivers(String... receivers) {
		this.receivers = Arrays.asList(receivers);
	}

	public MailEvent getEvent() {
		return event;
	}

	public void setEvent(MailEvent event) {
		this.event = event;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("MailItem{");
		sb.append("id='").append(id).append('\'');
		sb.append("title='").append(title).append('\'');
		sb.append(", body='").append(body).append('\'');
		sb.append(", attachment=").append(attachment);
		sb.append(", sender='").append(sender).append('\'');
		sb.append(", receivers=").append(receivers);
		sb.append(", event=").append(event);
		sb.append('}');
		return sb.toString();
	}
}
