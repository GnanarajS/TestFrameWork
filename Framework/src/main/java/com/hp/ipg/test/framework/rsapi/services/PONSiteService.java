package com.hp.ipg.test.framework.rsapi.services;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.PONSite;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;

import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Component
public class PONSiteService extends ResourceServiceBase{

	private final static String SITE_API_ENDPOINT = "/ponsite/sites";

	@Autowired
	TestConfiguration testConfiguration;

	@Override
	public String getEndpoint() {
		return SITE_API_ENDPOINT;
	}

	public Response createPONSite(PONSite ponSite, String token)
	{
		Response response = null;
		RequestSpecification request = given();
		ArrayList<PONSite> ponSiteRequest =new ArrayList<>();
		ponSiteRequest.add(ponSite);
		response = request.
				header(ResourceBase.AUTHORIZATION.toString(), token).
				header("Content-Type" , PONSite.getContentType()).
				body(ponSiteRequest).
				when().log().ifValidationFails(LogDetail.ALL).
				post(testConfiguration.getPONUserBaseUrI() + getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
		return response;		
	}

	public Response getSiteByUUID(String token, String siteUUID)
	{
		Response response = null;
		String requestURl = testConfiguration.getPONUserBaseUrI() + getEndpoint() + "/" + siteUUID;
		RequestSpecification request = given();
		return response = request.
				header(ResourceBase.AUTHORIZATION.toString(), token).
				accept(PONSite.getContentType()).
				when().log().ifValidationFails(LogDetail.ALL).
				get(requestURl).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
	}
	public static Object GetPONSiteObject(String objectName, PONSite ponSite, Class containerClass)
	{
		LinkedHashMap<String, String> config = ((LinkedHashMap<String, String>) ponSite.get(objectName));
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(config, containerClass);
	}

	/**
	 * This method return a string value from response body based on attribute name
	 * @param response: response body
	 * @param attributeName: name of the attribute against which value is retrieved
	 */
	public String getValueFromPONSiteResponse(ValidatableResponse response, String attributeName) throws JSONException
	{
		String attributeValue = null;
		JSONArray jsonarray = new JSONArray( response.extract().asString());;
		for (int i = 0; i < jsonarray.length(); i++) {
			JSONObject jsonobject = jsonarray.getJSONObject(i);
			attributeValue =  jsonobject.getString(attributeName);
		}
		return attributeValue;
	}
}
