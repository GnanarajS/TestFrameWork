package com.hp.ipg.test.framework.email;

import java.io.IOException;

import javax.mail.MessagingException;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


public abstract class EmailClientBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailClientBase.class);

	@Value("${test.emailTimeout:5}")
	protected int emailTimeoutMinutes;

	public abstract Document getEmailMessage(String toAddress, String subject, long timeoutSeconds) throws MessagingException, IOException;

	public abstract void deleteEmail(String toAddress, String subject, long timeoutSeconds) throws MessagingException, IOException;

	public boolean isEmailPresent(String toAddress, String subject, long timeoutSeconds) throws IOException, MessagingException {
		try {
			getEmailMessage(toAddress, subject, timeoutSeconds);
			LOGGER.info(String.format("Mail with subject %s found for user %s !", subject, toAddress));
		} catch (Exception e) {
			LOGGER.info(String.format("Mail with subject %s not found for user %s !", subject, toAddress));
			return false;
		}
		return true;
	}

	public int getEmailDefaultTimeoutSeconds() {
		return emailTimeoutMinutes * 60;
	}
}
