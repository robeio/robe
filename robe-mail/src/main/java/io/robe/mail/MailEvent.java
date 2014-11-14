package io.robe.mail;

/**
 * Mail event.
 */
public interface MailEvent {


	/**
	 * Called before the mail sending.
	 *
	 * @param item
	 */
	public void before(MailItem item);

	/**
	 * Called after the mail sending if it is succeeded.
	 *
	 * @param item
	 */
	public void success(MailItem item);

	/**
	 * Called after the mail sending if it is failed.
	 *
	 * @param item
	 * @param e    Reason of fail.
	 */
	public void error(MailItem item, Exception e);
}
