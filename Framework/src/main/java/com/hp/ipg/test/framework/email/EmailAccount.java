package com.hp.ipg.test.framework.email;

import java.io.IOException;

import javax.mail.MessagingException;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailAccount {
	private static final Logger  LOGGER = LoggerFactory.getLogger(EmailAccount.class);

	public static final Logger TEST = LoggerFactory.getLogger(EmailAccount.class);
	@Autowired
	protected EmailClientBase emailClient;

	public Document waitForEmailBySubject(String toAddress, String subject) throws MessagingException, IOException {
		return waitForEmailBySubject(toAddress, subject, emailClient.getEmailDefaultTimeoutSeconds());
	}

	public Document waitForEmailBySubject(String toAddress, String subject, long timeoutSeconds) throws MessagingException, IOException {
		LOGGER.info("Waiting for email with toAddress [{}] and subject [{}]...", toAddress, subject);

		Document doc = emailClient.getEmailMessage(toAddress, subject, timeoutSeconds);

		LOGGER.info("Found email with toAddress [{}] and subject [{}]...", toAddress, subject);
		return doc;
	}

	public boolean isEmailPresent(String toAddress, String subject) throws MessagingException, IOException {
		return emailClient.isEmailPresent(toAddress, subject, emailClient.getEmailDefaultTimeoutSeconds());
	}

	public void deleteEmail(String toAddress, String subject) throws MessagingException, IOException {
		deleteEmail(toAddress, subject, emailClient.getEmailDefaultTimeoutSeconds());
	}

	public void deleteEmail(String toAddress, String subject, long timeoutSeconds) throws MessagingException, IOException {
		LOGGER.info("Attempting to delete email with toAddress [{}] and subject [{}]...", toAddress, subject);

		emailClient.deleteEmail(toAddress, subject, timeoutSeconds);

		LOGGER.info("Deleted email with toAddress [{}] and subject [{}]", toAddress, subject);
	}
}
