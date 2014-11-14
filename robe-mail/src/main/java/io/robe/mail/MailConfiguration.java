package io.robe.mail;

public class MailConfiguration {
	private String host;
	private int port;
	private boolean auth;
	private String username;
	private String password;
	private boolean tlsssl;

	public MailConfiguration() {
		super();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isTlsssl() {
		return tlsssl;
	}

	public void setTlsssl(boolean tlsssl) {
		this.tlsssl = tlsssl;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("MailConfiguration{");
		sb.append("host='").append(host).append('\'');
		sb.append(", port=").append(port);
		sb.append(", auth=").append(auth);
		sb.append(", username='").append(username).append('\'');
		sb.append(", password='").append("*********\'");
		sb.append(", tlsssl=").append(tlsssl);
		sb.append('}');
		return sb.toString();
	}
}
