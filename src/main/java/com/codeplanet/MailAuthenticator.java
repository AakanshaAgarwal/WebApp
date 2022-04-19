package com.codeplanet;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {

	String username = null;
	String password = null;

	public MailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {

		return new PasswordAuthentication(username, password);
	}
}
