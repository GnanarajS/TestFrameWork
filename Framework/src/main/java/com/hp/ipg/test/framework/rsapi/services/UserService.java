package com.hp.ipg.test.framework.rsapi.services;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.User;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.aspectj.org.eclipse.jdt.internal.compiler.util.Util.UTF_8;

@Component
public class UserService extends ResourceServiceBase {

	private final static String CREATE_USER_ENDPOINT = "/scim/Users";
	public final static String USERNAME_PARAM = "username";
	public final static String PASSWORD_PARAM = "password";
	public final static String GRANT_TYPE_PARAM = "grant_type";
	private final static String TOKEN_ENDPOINT = "/cps/openid/v1/token";
	private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
	public final static String ACCEPT = "application/json";
	private final static String RESPONSE_MESSAGE = "responseMessage";
	private final static String RESOURCES = "Resources";
	private final static String ID = "id";
	private final static String ROAM_USER = "Roam-User";
	public final static String AUTHORIZATION = "Basic NzVjODllMjAtNTliNy00ZDAxLTg5OTEtZGUxYWU3NDU5Yjc4OjdjNjdkM2Y3LTk0NDctNDliNC1iYThjLWVlMjhkNjQxYjE3Yw==";
	private final static String GET_USER_NAME_FILTER_ENDPOINT_URL = CREATE_USER_ENDPOINT + "?filter=";
	private final static String USER_NAME_QUERY = "userName sw \"%s\"";
	private final static String USER_NAME_QUERY_WITH_EQUAL = "userName eq \"%s\"";
	private final static String CLIENT_ID = "client_id";
	private final static String CLIENT_SECRET = "client_secret";
	private final static String REFRESH_TOKEN = "refresh_token";
	public  final static String TOKEN_TYPE = "token_type";
	public  final static String ACCESS_TOKEN ="access_token";

	private ArrayList<String> listOfTestUserId = new ArrayList<String>();

	@Autowired
	TestConfiguration testConfiguration;

	@Override
	public String getEndpoint() {
		return testConfiguration.getPONUserBaseUrI()+CREATE_USER_ENDPOINT;
	}

	public String generateBearerToken(User user) {

		Response response= getLoginResponse(user);

		String tokenType = response.getBody().jsonPath().get(TOKEN_TYPE);
		String bearerToken = response.getBody().jsonPath().get(ACCESS_TOKEN);
		bearerToken = tokenType + ' ' + bearerToken;
		return bearerToken;
	}

	public Response getLoginResponse(User user){
		String userName = user.getUserNameFromResponse();
		String password = ResourceBase.DEFAULT_PASSWORD;
		String grantType = "password";

		RequestSpecification request = given();
		request.header("Content-Type" , CONTENT_TYPE);
		request.header("Accept",ACCEPT);
		request.header("Authorization",AUTHORIZATION);

		return request.
				param(USERNAME_PARAM,userName).
				param(PASSWORD_PARAM,password).
				param(GRANT_TYPE_PARAM,grantType).
				when().log().ifValidationFails(LogDetail.ALL).
				post(TOKEN_ENDPOINT).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
	}

	public String getRefreshTokenFromResponse(User user){
		Response response=getLoginResponse(user);
	 	String refreshToken = response.getBody().jsonPath().get(REFRESH_TOKEN);
		return refreshToken;
	}

	public  String getRefreshToken(User user){
		String grantType = "refresh_token";
		String refreshToken=getRefreshTokenFromResponse(user);

		RequestSpecification request = given();
		Response response=request.
				param(CLIENT_ID,testConfiguration.getClientID() ).
				param(CLIENT_SECRET,testConfiguration.getClientSecret()).
				param(GRANT_TYPE_PARAM,grantType).
				param(REFRESH_TOKEN,refreshToken).
				when().log().ifValidationFails(LogDetail.ALL).
				post(TOKEN_ENDPOINT).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();

		String tokenType = response.getBody().jsonPath().get(TOKEN_TYPE);
		String bearerToken = response.getBody().jsonPath().get(ACCESS_TOKEN);
		bearerToken = tokenType + ' ' + bearerToken;
		return bearerToken;

	}

	public String getBearerToken(String userName) {

		String grantType = "password";

		RequestSpecification request = given();
		request.header("Content-Type" , CONTENT_TYPE);
		request.header("Accept",ACCEPT);
		request.header("Authorization",AUTHORIZATION);

		Response response = request.
				param(USERNAME_PARAM,userName).
				param(PASSWORD_PARAM,User.DEFAULT_PASSWORD).
				param(GRANT_TYPE_PARAM,grantType).
				when().log().ifValidationFails(LogDetail.ALL).
				post(TOKEN_ENDPOINT).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();

		String tokenType = response.getBody().jsonPath().get(TOKEN_TYPE);
		String bearerToken = response.getBody().jsonPath().get(ACCESS_TOKEN);
		bearerToken = tokenType + ' ' + bearerToken;
		return bearerToken;

	}

	public ArrayList<String> getListOfTestUserId() throws IOException {

		String userNameQuery = String.format(USER_NAME_QUERY, ROAM_USER);
		userNameQuery = URLEncoder.encode(userNameQuery);
		String requestUrl = GET_USER_NAME_FILTER_ENDPOINT_URL + userNameQuery;

		RequestSpecification request = given();

		ValidatableResponse response = request.
				accept(User.getContentType()).
				get(requestUrl).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value());

		JSONObject jsonResponse = new JSONObject(new JSONObject(response.extract().asString()).getString(RESPONSE_MESSAGE));
		JSONArray resourceArray = (JSONArray) jsonResponse.getJSONArray(RESOURCES);

		for (int i = 0; i < resourceArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) resourceArray.get(i);

			String id = jsonObject.getString(ID);
			listOfTestUserId.add(id);
		}
		return listOfTestUserId;
	}

	public void deleteUserAccount(User user) {

		String userId = user.getUserId().toString();
		String userUrl = testConfiguration.getPONUserBaseUrI()+CREATE_USER_ENDPOINT + "/" + userId;

		RequestSpecification request = given();

		ValidatableResponse response = request.
				accept(User.getContentType()).
				header("Authorization",AUTHORIZATION).
				when().log().all().
				delete(userUrl).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.NO_CONTENT.value());

	}

    public String getUserIdFromUserName(String userName) throws IOException {
        String userNameQuery = String.format(USER_NAME_QUERY_WITH_EQUAL, userName);
        userNameQuery = UriUtils.encodeQueryParam(userNameQuery, UTF_8);
        String requestUrl = GET_USER_NAME_FILTER_ENDPOINT_URL + userNameQuery;
        RequestSpecification request = given();

        ValidatableResponse response = request.
                accept(User.getContentType()).
                when().log().ifValidationFails(LogDetail.ALL).
                get(requestUrl).
                then().log().ifValidationFails(LogDetail.ALL).
                statusCode(HttpStatus.OK.value());

        User user = User.getInstance();
        return user.getUserIdFromResponse(response);
    }

    public User getUserFromUserName(String userName) throws IOException {
        String requestUrl = CREATE_USER_ENDPOINT + "/" + getUserIdFromUserName(userName);

        ValidatableResponse response = given().
                accept(User.getContentType()).
                when().log().ifValidationFails(LogDetail.ALL).
                get(requestUrl).
                then().log().ifValidationFails(LogDetail.ALL).
                statusCode(HttpStatus.OK.value());
        return response.extract().body().as(User.class);
    }

	public User createUserUsingUserNameAndPassword(String userName, String password) throws IOException {
		User createUser = User.getInstance();
		createUser.setUserName(userName);
		createUser.setPassword(password);
		ValidatableResponse response = given().
				accept(User.getContentType()).
				header("Authorization",AUTHORIZATION).
				header("Content-Type",User.getContentType()).
				when().log().ifValidationFails(LogDetail.ALL).
				body(createUser).
				post(getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.CREATED.value());

		return response.extract().body().as(User.class);
	}

	public User create(User user) {

		return given().
				contentType(user.getContentType()).
				header("Authorization",AUTHORIZATION).
				header("Content-Type",User.getContentType()).
				body(user).
				when().log().ifValidationFails(LogDetail.ALL).
				post(getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.CREATED.value()).
				extract().body().as(User.class);
	}

	public Response checkUserLogin(String userName, String password, int expectedCode) {
		String grantType = "password";

		RequestSpecification request = given();
		request.header("Content-Type" , CONTENT_TYPE);
		request.header("Accept",ACCEPT);
		request.header("Authorization",AUTHORIZATION);

		return request.
				param(USERNAME_PARAM,userName).
				param(PASSWORD_PARAM,password).
				param(GRANT_TYPE_PARAM,grantType).
				when().log().ifValidationFails(LogDetail.ALL).
				post(TOKEN_ENDPOINT).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(expectedCode).extract().response();
	}
}