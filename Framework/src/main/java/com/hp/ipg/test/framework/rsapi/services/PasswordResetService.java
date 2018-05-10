package com.hp.ipg.test.framework.rsapi.services;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.PasswordReset;
import com.hp.ipg.test.framework.rsapi.resources.User;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;

import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;

@Component
public class PasswordResetService extends ResourceServiceBase{

	public static final String PASSWORD_RESET_ENDPOINT = "/passwordreset/reset";

	@Autowired
	TestConfiguration testConfiguration;

	@Autowired
	protected UserService userService;

	@Override
	public String getEndpoint() {
		// TODO Auto-generated method stub
		return null;
	}

	public Response sendPasswordResetMail(User user, String mailId) throws IOException {
		PasswordReset passwordReset = PasswordReset.getInstance();

		user.setUserName(mailId);
		passwordReset.setEmail(mailId);
		user = userService.create(user);

		String requestUrl = testConfiguration.getPONUserBaseUrI() + PASSWORD_RESET_ENDPOINT;

		return given().
				accept(User.getContentType()).
				contentType(User.getContentType()).
				header(ResourceBase.AUTHORIZATION, ResourceBase.PASSWORD_RESET_AUTHORIZATION).
				body(passwordReset).
				when().log().ifValidationFails(LogDetail.ALL).
				post(requestUrl).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(204).extract().response();

	}
}
