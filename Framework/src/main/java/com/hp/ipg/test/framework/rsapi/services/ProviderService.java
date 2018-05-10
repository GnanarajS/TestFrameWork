package com.hp.ipg.test.framework.rsapi.services;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.Provider;
import com.hp.ipg.test.framework.rsapi.resources.User;
import com.hp.ipg.test.framework.rsapi.resources.base.HateoasResource;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Component
public class ProviderService extends ResourceServiceBase {

	private final static String PROVIDERS = "providers";
	private static final String PROVIDER_API_ENDPOINT = "/" + PROVIDERS;
	private static final String PROVIDER_BY_USER_API_ENDPOINT = PROVIDER_API_ENDPOINT + "?usernameHash=";
	private final static String LINK_PROVIDER_TO_USER = PROVIDER_API_ENDPOINT + "/%s/users/%s";
	private static final String SERVICE_DESCRIPTION_LINK = "/cps/restapi/servicedescription?clientAPIver=2.0";
	private static final String SERVICE_DESCRIPTION_WITH_PROVIDER_UUID_LINK = "/servicedescription/%s?clientAPIVer=2.0";

	@Autowired
	TestConfiguration testConfiguration;

	@Override
	public String getEndpoint() {
		return PROVIDER_API_ENDPOINT;
	}

	public Response getProviders() 
	{
		RequestSpecification request = given();
		return request.
				accept(Provider.getContentType()).
				when().log().ifValidationFails(LogDetail.ALL).
				get(testConfiguration.getLookUpBaseURI() + getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
	}

	public Response linkGivenProviderForUser(User user, String providerUUID) 
	{
		String userNameHash = ResourceBase.getMD5Hash(user.getUserName());
		String requestURL = String.format(testConfiguration.getLookUpBaseURI() + LINK_PROVIDER_TO_USER, providerUUID, userNameHash);
		RequestSpecification request = given();      
		return request.
				accept(Provider.getContentType()).
				when().log().ifValidationFails(LogDetail.ALL).
				post(requestURL).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
	}

	public String getServiceDescriptionFromUser(User user) throws IOException {
		Provider provider=Provider.getInstance();
		String providerUUID= String.valueOf(UUID.randomUUID());
		provider.setProviderUUID(providerUUID);
		provider.setProviderType("0");
		provider.setProviderName("API Testing");
		addProvider(provider);

		linkGivenProviderForUser(user,providerUUID);
		String userNameHash = ResourceBase.getMD5Hash(user.getUserNameFromResponse());	
		String requestURl = testConfiguration.getLookUpBaseURI() + PROVIDER_BY_USER_API_ENDPOINT + userNameHash;
		RequestSpecification request = given();
		Response response = request.
				accept(Provider.getContentType()).
				when().log().ifValidationFails(LogDetail.ALL).
				get(requestURl).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
		return getServiceDescriptionLink(response);
	}

	public String getDefaultProvider(){
	    String providerUUId = null;
		Response response = getProviders();

		ArrayList providerList = response.getBody().jsonPath().get("providers");

		for(int i=0; i<providerList.size();i++) {
			HashMap providerData = ((HashMap) providerList.get(i));

			if (providerData.get("defaultProvider").toString().equals("true")) {
				ArrayList links = (ArrayList) providerData.get("links");
				providerUUId = providerData.get("providerUUID").toString();
				break;
			}
		}
		return  providerUUId;
	}

	public String getDefaultServiceDescriptionUrlFromUser(User user) throws IOException {
		linkGivenProviderForUser(user,getDefaultProvider());

		String userNameHash = ResourceBase.getMD5Hash(user.getUserNameFromResponse());
		String requestURl = testConfiguration.getLookUpBaseURI() + PROVIDER_BY_USER_API_ENDPOINT + userNameHash;
		RequestSpecification request = given();
		Response response = request.
				accept(Provider.getContentType()).
				when().log().ifValidationFails(LogDetail.ALL).
				get(requestURl).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
		return getServiceDescriptionLink(response);
	}

	public String getServiceDescriptionLink(Response response) 
	{
		ArrayList attribute = response.getBody().jsonPath().get(PROVIDERS);
		HashMap providerData = (HashMap) attribute.get(0);
		ArrayList links = (ArrayList) providerData.get(HateoasResource.LINKS);
		HashMap allLink = (HashMap) links.get(0);
		String serviceDescriptionLink = (String) allLink.get(HateoasResource.LINK_HREF);
		return serviceDescriptionLink;
	}

	public Response getProviderByUUID(String UUID) 
	{
		String requestURl = testConfiguration.getLookUpBaseURI() + getEndpoint() + "/" + UUID;
		RequestSpecification request = given();
		return request.
				accept(Provider.getContentType()).
				when().log().ifValidationFails(LogDetail.ALL).
				get(requestURl);
	}


	public Response getProviderByUser(User user)
	{
		String userNameHash = new ResourceBase().getMD5Hash(user.getUserNameFromResponse());
		String requestUrl = testConfiguration.getLookUpBaseURI() + PROVIDER_BY_USER_API_ENDPOINT + userNameHash;
		RequestSpecification request = given();
		return request.
				accept(Provider.getContentType()).
				get(requestUrl).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
	}

	public Response addProviderWithUUID(Provider provider)
	{
		Response response = null;
		RequestSpecification request = given();		
		String hrefLink = String.format(testConfiguration.getBaseUri() + SERVICE_DESCRIPTION_WITH_PROVIDER_UUID_LINK, provider.getProviderUUID());
		provider.getLinks()[0].setHref(hrefLink);	
		response = request.
				header("Content-Type" , Provider.getContentType()).
				body(provider).
				when().log().ifValidationFails(LogDetail.ALL).
				post(testConfiguration.getLookUpBaseURI() + getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
		return response;		
	}

	public Response addProvider(Provider provider)
	{
		Response response = null;
		RequestSpecification request = given();
		String hrefLink = String.format(testConfiguration.getBaseUri() + SERVICE_DESCRIPTION_LINK, provider.getProviderUUID());
		provider.getLinks()[0].setHref(hrefLink);
		response = request.
				header("Content-Type" , Provider.getContentType()).
				body(provider).
				when().log().ifValidationFails(LogDetail.ALL).
				post(testConfiguration.getLookUpBaseURI() + getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
		return response;
	}

	public void removeProvider(String providerUUID)
	{
		RequestSpecification request = given();
		String requestUrl = testConfiguration.getLookUpBaseURI() + PROVIDER_API_ENDPOINT + "/" + providerUUID;
		request.accept(User.getContentType()).
		delete(requestUrl).
		then().log().ifValidationFails(LogDetail.ALL).
		statusCode(HttpStatus.OK.value());
	}
}
