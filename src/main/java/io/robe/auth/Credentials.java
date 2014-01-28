package io.robe.auth;

import com.yammer.dropwizard.auth.basic.BasicCredentials;

import java.util.Set;


public class Credentials extends BasicCredentials {

	private Set<String> permissions;

	public Credentials(String username, String password, Set<String> permissions) {
		super(username, password);
		this.permissions = permissions;
	}

	public Credentials(String username, String password) {
		super(username, password);
	}
	public Credentials(){
		super("", "");
	}

	@Override
	public String toString() {
		return "username=" + getUsername();
	}

	public Set<String> getPermissions() {
		return permissions;
	}
}

