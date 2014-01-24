package io.robe.auth;

import com.yammer.dropwizard.auth.basic.BasicCredentials;


public class Credentials extends BasicCredentials {

	public Credentials() {
		super("", "");
	}

	public Credentials(String username, String password) {
		super(username, password);
	}

	@Override
	public String toString() {
		return "username=" + getUsername();
	}
}

