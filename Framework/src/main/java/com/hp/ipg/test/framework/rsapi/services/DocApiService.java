package com.hp.ipg.test.framework.rsapi.services;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.User;
import io.restassured.filter.log.LogDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.hp.ipg.test.framework.rsapi.resources.DocProcess;
import com.hp.ipg.test.framework.rsapi.resources.DocProcess.Property;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Component
public class DocApiService extends ResourceServiceBase {
	public static final String DOC_API_ENDPOINT = "/cps/DocApi";

	@Autowired
	protected TestConfiguration testConfiguration;

	@Autowired
	UserService userService;

	@Autowired
	DocStatusService docStatusService;

	@Override
	public String getEndpoint() {
		return DOC_API_ENDPOINT;
	}

	public ValidatableResponse createJob(String token) throws IOException {
		RequestSpecification request = given();

		request.header(ResourceBase.AUTHORIZATION, token);
		DocProcess docProcess = DocProcess.getInstance();

		return request.
				formParam(Property.docAPIfunc.toString(), docProcess.getDocApiFunction()).
				formParam(Property.clientSWProtocol.toString(), docProcess.getClientSWProtocol()).
				formParam(Property.clientSWKey.toString(), docProcess.getCientSWKey()).
				formParam(Property.clientSWName.toString(), docProcess.getClientSWName()).
				formParam(Property.clientSWVer.toString(), docProcess.getClientSWVer()).
				formParam(Property.userLang.toString(), docProcess.getUserLang()).
				formParam(Property.jobDestination.toString(), testConfiguration.getPoolDestination()).
				multiPart(Property.documentURI.toString(), docProcess.getDocumentURI()).
				when().log().ifValidationFails().
				post(getEndpoint()).
				then().log().ifValidationFails().
				statusCode(HttpStatus.OK.value());
	}

	public ValidatableResponse createJob(String URL, String userName) throws IOException {
		RequestSpecification request = given();

		String token = userService.getBearerToken(userName);

		request.header(ResourceBase.AUTHORIZATION, token);

		DocProcess docProcess = DocProcess.getInstance();

		return request.
				formParam(Property.docAPIfunc.toString(), docProcess.getDocApiFunction()).
				formParam(Property.clientSWProtocol.toString(), docProcess.getClientSWProtocol()).
				formParam(Property.clientSWKey.toString(), docProcess.getCientSWKey()).
				formParam(Property.clientSWName.toString(), docProcess.getClientSWName()).
				formParam(Property.clientSWVer.toString(), docProcess.getClientSWVer()).
				formParam(Property.userLang.toString(), docProcess.getUserLang()).
				formParam(Property.jobDestination.toString(), testConfiguration.getPoolDestination()).
				multiPart(Property.documentURI.toString(), URL).
				when().log().ifValidationFails(LogDetail.ALL).
				post(getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value());
	}

}