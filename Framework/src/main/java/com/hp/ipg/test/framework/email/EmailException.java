package com.hp.ipg.test.framework.email;

import javax.mail.MessagingException;

public class EmailException extends MessagingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailException(String message) {
		super(message);
	}

	public EmailException(String message, Exception innerException) {
		super(message, innerException);
	}
}
