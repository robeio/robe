package io.robe.auth;

import com.yammer.dropwizard.auth.basic.BasicCredentials;

import java.util.Collections;
import java.util.Set;


/**
 * Basic Credential model for Auth operations. Secure and lightweight injectable model.
 */
public class Credentials extends BasicCredentials {

	private Set<String> permissions;

	/**
	 * Creates an instance with the given parameters.
	 *
	 * @param username    Username of the user.
	 * @param password    Password of the user.
	 * @param permissions Permission list of the user.
	 */
	public Credentials(String username, String password, Set<String> permissions) {
		this(username, password);
		this.permissions = Collections.unmodifiableSet(permissions);
	}

	/**
	 * Creates an instance with the given parameters.
	 * @param username  Username of the user.
	 * @param password  Password of the user.
	 */
	public Credentials(String username, String password) {
		super(username, password);
	}

	/**
	 * Default constructor for Jackson Parser.
	 * Do  <b> not</b> use. Use {@link #Credentials(String, String, java.util.Set)} instead.
	 */
	public Credentials() {
		super("", "");
	}

	/**
	 * Gives an unmodifiable set of permissions.
	 * @return  Unmodifiable set
	 */
	public Set<String> getPermissions() {
		return permissions;
	}
}

