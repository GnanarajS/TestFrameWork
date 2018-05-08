package com.hp.ipg.test.framework.rsapi.services;

import static io.restassured.RestAssured.given;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;

import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Component
public class AuthenticationService extends ResourceServiceBase {
	
	public static final String GET_AUTHENTICATE_JWT_ENDPOINT = "/cps/authenticate";
	public static final String ACCESS_TOKEN = "access_token";
	
	@Override
	public String getEndpoint() {
		return GET_AUTHENTICATE_JWT_ENDPOINT;
	}
	
	public String getJWTAccessToken(String bearerToken) {
		
		RequestSpecification request = given();

		Response response = request.
				header(ResourceBase.AUTHORIZATION.toString(), bearerToken).
				when().log().ifValidationFails(LogDetail.ALL).
				get(getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
		
		String JWTAccessToken = response.getBody().jsonPath().get(ACCESS_TOKEN);
		return JWTAccessToken;
	}
	
}
