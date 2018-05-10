package com.hp.ipg.test.framework.email;

import static com.jayway.awaitility.Awaitility.await;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MailinatorEmailClient extends EmailClientBase {

	private static final String MAILINATOR_API_ENDPOINT = "https://api.mailinator.com/api";
	private static final String MAILINATOR_INBOX_URL = MAILINATOR_API_ENDPOINT + "/inbox?to=%s&token=%s&private_domain=false";
	private static final String MAILINATOR_EMAIL_URL = MAILINATOR_API_ENDPOINT + "/email?id=%s&token=%s&private_domain=false";
	private static final String MAILINATOR_DELETE_EMAIL_URL = MAILINATOR_API_ENDPOINT + "/delete?id=%s&token=%s&private_domain=false";
	private static final String MAIL_TOKEN = "1a61fc8c06df44bc9b1d4b6f278fbc30";

	private static final String EMAIL_MSG_DATA = "data";
	private static final String DELETE_EMAIL_STATUS = "status";
	private static final String DELETE_EMAIL_STATUS_VALUE = "ok";

	private static final Logger LOGGER = LoggerFactory.getLogger(MailinatorEmailClient.class);

	@Override
	public Document getEmailMessage(String toAddress, String subject, long timeoutSeconds) throws IOException, MessagingException {
		Document doc = null;

		try {
			String msgId = waitForEmailMsgId(toAddress, subject, timeoutSeconds);
			JSONObject msgDataObj = getResponse(String.format(MAILINATOR_EMAIL_URL, msgId, MAIL_TOKEN));
			JSONObject dataSection = (JSONObject) msgDataObj.get(EMAIL_MSG_DATA);
			doc = getEmailBody(dataSection);
		} catch (JSONException e) {
			LOGGER.error("JSONException exception occurred while receiving mail", e);
		}
		if (doc == null) {
			throw new MessagingException("Exception occurred while getting email message");
		}
		return doc;
	}

	@Override
	public void deleteEmail(String toAddress, String subject, long timeoutSeconds) throws MessagingException, IOException {
		try {
			String msgId = waitForEmailMsgId(toAddress, subject, timeoutSeconds);
			JSONObject msgDataObj = getResponse(String.format(MAILINATOR_DELETE_EMAIL_URL, msgId, MAIL_TOKEN));
			String deleteStatus = msgDataObj.get(DELETE_EMAIL_STATUS).toString();
			if (!DELETE_EMAIL_STATUS_VALUE.equals(deleteStatus)) {
				throw new MessagingException(String.format("Response from mailinator was [%s], not the expected [%s]", deleteStatus, DELETE_EMAIL_STATUS_VALUE));
			}
		} catch (JSONException e) {
			throw new MessagingException("JSONException exception occurred while checking mail to delete", e);
		}
	}

	private String waitForEmailMsgId(String toAddress, String subject, long timeoutSeconds) throws IOException, JSONException {
		String emailUrl = String.format(MAILINATOR_INBOX_URL, toAddress, MAIL_TOKEN);
		await().atMost(timeoutSeconds, TimeUnit.SECONDS).until(checkMsgID(emailUrl, subject));
		return getMsgId(emailUrl, subject);
	}

	private Document getEmailBody(JSONObject dataSectionObj) throws JSONException, MessagingException {

		final String EMAIL_MSG_PARTS = "parts";
		final String EMAIL_MSG_HEADER = "headers";
		final String CONTENT_TYPE = "content-type";
		final String MSG_CONTENT_TYPE = "text/html";
		final String EMAIL_BODY = "body";

		Document emailMsgBody = null;
		JSONArray msgJSONParts = (JSONArray) dataSectionObj.get(EMAIL_MSG_PARTS);

		if (msgJSONParts.length() > 1) {
			for (int msgJSONPartIndex = 0; msgJSONPartIndex < msgJSONParts.length(); msgJSONPartIndex++) {
				JSONObject msgJSONPartObj = (JSONObject) msgJSONParts.get(msgJSONPartIndex);
				JSONObject JSONPartHeaders = (JSONObject) msgJSONPartObj.get(EMAIL_MSG_HEADER);
				if (JSONPartHeaders.get(CONTENT_TYPE).toString().contains(MSG_CONTENT_TYPE)) {
					emailMsgBody = Jsoup.parse(msgJSONPartObj.get(EMAIL_BODY).toString());
					break;
				}
			}
		} else {
			JSONObject msgJSONPartObj = (JSONObject) msgJSONParts.get(0);
			emailMsgBody = Jsoup.parse(msgJSONPartObj.get(EMAIL_BODY).toString());
		}
		if (emailMsgBody == null) {
			throw new MessagingException("Message body is null");
		}
		return emailMsgBody;

	}

	private static JSONObject getResponse(String url) throws IOException, JSONException {
		URLConnection connection = new URL(url).openConnection();
		InputStream response = connection.getInputStream();
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(response));
		StringBuilder responseStrBuilder = new StringBuilder();
		String inputStr;
		while ((inputStr = streamReader.readLine()) != null) {
			responseStrBuilder.append(inputStr);
		}
		return new JSONObject(responseStrBuilder.toString());
	}

	private static String getMsgId(String url, String subject) throws IOException, JSONException {
		final String EMAIL_MESSAGES = "messages";
		final String EMAIL_SUBJECT = "subject";
		final String EMAIL_ID = "id";
		String msgId = null;

		// Unfortunately mailinator doesn't remove CRLF from the subject in the inbox api,
		// this regex replace ensures we can still find the email by subject
		String subjectRegex = subject.replaceAll("\\s", "\\\\s\\+");

		JSONObject msgInboxObj = getResponse(url);
		JSONArray msgInboxArr = (JSONArray) msgInboxObj.get(EMAIL_MESSAGES);
		for (int i = 0; i < msgInboxArr.length(); i++) {
			JSONObject messagesObj = msgInboxArr.getJSONObject(i);
			String messageSubject = messagesObj.get(EMAIL_SUBJECT).toString();
			if (messageSubject.matches(subjectRegex)) {
				msgId = messagesObj.get(EMAIL_ID).toString();
				break;
			}
		}
		return msgId;
	}

	private Callable<Boolean> checkMsgID(final String url, final String subject) {
		return new Callable<Boolean>() {
			public Boolean call() throws Exception {
				String msgId = getMsgId(url, subject);
				return msgId != null;
			}
		};
	}
}
