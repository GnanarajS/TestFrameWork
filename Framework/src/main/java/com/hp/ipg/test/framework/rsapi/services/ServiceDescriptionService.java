package com.hp.ipg.test.framework.rsapi.services;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.ServiceDescription;
import com.hp.ipg.test.framework.rsapi.resources.User;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import static io.restassured.RestAssured.given;

@Component
public class ServiceDescriptionService extends ResourceServiceBase 
{
	private static final String SERVICE_DESCRIPTION_ENDPOINT = "/servicedescription";
	private static final String SERVICE_DESCRIPTION_WITH_PROVIDER = SERVICE_DESCRIPTION_ENDPOINT + "/%s?clientAPIVer=2.0";

	@Autowired
	ProviderService providerService;

	@Autowired
	TestConfiguration testConfiguration;

	@Override
	public String getEndpoint() {
		return SERVICE_DESCRIPTION_ENDPOINT;
	}

	public Response getServiceDescription(User user) throws IOException {
		String url = providerService.getServiceDescriptionFromUser(user);

		RequestSpecification request = given();

		return request.
				accept(User.getContentType()).
				when().log().ifValidationFails(LogDetail.ALL).
				get(url).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
	}

	public String getLinkfromServiceDescription(User user, String urlName) throws IOException {

		Response response = getServiceDescription(user);
		ArrayList serviceList = response.getBody().jsonPath().get("services");
		HashMap providerData = ((HashMap) serviceList.get(0));

		ArrayList links = new ArrayList();
		ArrayList list = new ArrayList();
		if (providerData.containsKey(HateoasResource.LINKS)) {
			links = (ArrayList) providerData.get("links");
		}
		HashMap oauth = (HashMap) providerData.get("access");
		HashMap  attribute = (HashMap) oauth.get("oauth2");
		list=(ArrayList)attribute.get("links");
		list.addAll(links);
		String endpoint = "";
		for (int i = 0; i < list.size(); i++) {
			HashMap linkValues = (HashMap) list.get(i);

			if (linkValues.get("href").toString().contains(urlName)) {
				endpoint = linkValues.get("href").toString();
				break;
			}

		}

		return  endpoint;
	}

	public Response addServiceDescriptionByProvider(ServiceDescription serviceDescriptionRequest, String providerUUID)
	{
		String requestUrl = String.format(testConfiguration.getLookUpBaseURI() + SERVICE_DESCRIPTION_WITH_PROVIDER, providerUUID);
		Response response = null;
		RequestSpecification request = given();	
		serviceDescriptionRequest.getLinks()[0].setHref(testConfiguration.getBaseUri() + DirSearchService.DIR_SEARCH_ENDPOINT);
		serviceDescriptionRequest.getLinks()[1].setHref(testConfiguration.getBaseUri() + DocApiService.DOC_API_ENDPOINT);

		response = request.
				header("Content-Type" , ServiceDescription.getContentType()).
				body(serviceDescriptionRequest).
				when().log().ifValidationFails(LogDetail.ALL).
				post(requestUrl).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
		return response;	
	}
}
