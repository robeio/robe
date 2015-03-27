package io.robe.mail;

import java.util.HashMap;

public class MailConfiguration {
	private String usernameKey;
	private String passwordKey;
	private HashMap<String,Object> properties;

	public MailConfiguration() {
		super();
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	public String getUsernameKey() {
		return usernameKey;
	}

	public void setUsernameKey(String usernameKey) {
		this.usernameKey = usernameKey;
	}

	public String getPasswordKey() {
		return passwordKey;
	}

	public void setPasswordKey(String passwordKey) {
		this.passwordKey = passwordKey;
	}
}
