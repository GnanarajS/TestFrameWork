package com.hp.ipg.rsapi.test.base.tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.hp.ipg.test.framework.genericLib.testExecution.TestBase;
import com.hp.ipg.test.framework.rsapi.APITestSuiteBase;
import io.restassured.response.ValidatableResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.lang.IllegalArgumentException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ipg.rsapi.test.base.config.CaseInsensitiveContainsMatcher;
import com.hp.ipg.rsapi.test.base.config.CaseInsensitiveEndsWithMatcher;
import com.hp.ipg.rsapi.test.base.config.CaseInsensitiveStartsWithMatcher;
import com.hp.ipg.rsapi.test.base.config.RsApiBaseTestConfiguration;
import com.hp.ipg.rsapi.test.base.dataModels.DataProviderBaseModel;
import com.hp.ipg.rsapi.test.base.dataModels.DataProviderBaseModel.Property;
import com.hp.ipg.rsapi.test.base.dataModels.DataProviderQueryStringModel;
import org.testng.annotations.Listeners;

/**
 * This class provides common functionality to validate response data which is common to all endpoints and version
 */

@Listeners({
		APITestSuiteBase.class,
		com.hp.ipg.test.framework.genericLib.testExecution.TestListener.class,
		com.hp.ipg.test.framework.genericLib.testExecution.reporting.JiraReportingClient.class,
		com.hp.ipg.test.framework.genericLib.testExecution.reporting.TriageReport.class })
@ContextConfiguration(classes = RsApiBaseTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class APITestBase extends TestBase {
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(APITestBase.class);
	public final static String CONTAINS = "contains";
	public final static int ZERO_INDEX = 0;
	public static final String LINK_REL = "rel";
	public static final String LINK_HREF = "href";
	public static final String UTF_8 = "UTF-8";

	@AfterMethod(alwaysRun = true)
	public void afterTest(ITestResult result) throws JsonProcessingException {
		//log test failure for better tracking in run output
		if (result.getStatus() == ITestResult.FAILURE) {
			Object[] params = result.getParameters();
			if (params != null && params.length > 0) {
				ObjectMapper mapper = new ObjectMapper();
				LOGGER.error("Test Failure Detail: " + result.getMethod().getMethodName() + " : " + mapper.writeValueAsString(params[0]));
			}
		}
	}

	/**
	 * This methods checks the data type of the value and based on that calls appropriate validate method
	 * 
	 * @param response
	 *            : response came from the test method
	 * @param property
	 *            : property to validate coming from response
	 * @param value
	 *            : expected value, it could an Integer, String, String with wild card or Array of String
	 */
	private void validateProperty(ValidatableResponse response, String property, Object value, String type) {
		if (value instanceof Integer) {
			if (StringUtils.isNotEmpty(type) && type.equals(Property.GREATER_THAN_OR_EQUALTO.toString())) {
				validatePropertyGreaterThanEqual(response, property, (Integer) value);
			}
			else if (StringUtils.isNotEmpty(type) && type.equals(Property.LESS_THAN_OR_EQUALTO.toString())) {
				validatePropertyLessThanEqual(response, property, (Integer) value);
			}
			else {
				validateProperty(response, property, (Integer) value);
			}
		} else if (value instanceof String) {
			String valueToCheck = (String) value;
			if (StringUtils.isNotEmpty(type) && type.equals(Property.PROPERTY_MATCHER.toString())) {
				String filterValue = response.extract().path(valueToCheck);
				String filterAttribute = response.extract().path(property);
				Assert.assertEquals(filterValue, filterAttribute);
			}
			else if (valueToCheck.indexOf(DataProviderBaseModel.Property.WILDCARD.toString()) != -1) {
				Object expectedValueArray = response.extract().body().jsonPath().get(property);
				if (!(expectedValueArray instanceof List)) {
					validatePropertyWithStringResponse(response, property, valueToCheck);
				} else {
					validatePropertyWithArrayResponseValues(response, property, valueToCheck);
				}
			} else {
				validateProperty(response, property, valueToCheck);
			}
		} else if (value instanceof ArrayList) {
			Object responseValues = response.extract().body().jsonPath().get(property);
			if (StringUtils.isNotEmpty(type) && type.equalsIgnoreCase(CONTAINS)) {
				validatePropertyArrayWithWildCard((List<String>) responseValues, (ArrayList<String>) value);
			} else {
				validateProperty(response, new HashSet<String>((List<String>) responseValues), (ArrayList<String>) value);
			}
		}
	}

	private void extractLinksFromResponse(String linkRelNameToValidate, ValidatableResponse response, String filterAttribute, Set<String> actualLinkvalues) {
		Object listOfLinkMapsOrListOfListOfMaps = response.extract().path(filterAttribute);
		if (listOfLinkMapsOrListOfListOfMaps instanceof ArrayList) {
			ArrayList arrayListOfLinkMapsOrListOfArrayListOfLinkMaps = (ArrayList) listOfLinkMapsOrListOfListOfMaps;
			extractResponseAsListOfMapsOrListOfListOfMaps(arrayListOfLinkMapsOrListOfArrayListOfLinkMaps, linkRelNameToValidate, actualLinkvalues);
		} else {
			throw new IllegalArgumentException("Extracting response through the filterAttribute passed did not return an ArraList");
		}
	}

	private boolean responseContainsSingleRecord(List arrayListOfLinkMapsOrListOfArrayListOfLinkMaps) {
		return arrayListOfLinkMapsOrListOfArrayListOfLinkMaps.get(ZERO_INDEX) instanceof HashMap;
	}

	private boolean responseContainsMultipleRecords(List arrayListOfLinkMapsOrListOfArrayListOfLinkMaps) {
		return arrayListOfLinkMapsOrListOfArrayListOfLinkMaps.get(ZERO_INDEX) instanceof ArrayList;
	}

	private void extractResponseAsListOfMapsOrListOfListOfMaps(List arrayListOfLinkMapsOrListOfArrayListOfLinkMaps, String linkRelNameToValidate, Set<String> actualLinkvalues) {
		if (responseContainsSingleRecord(arrayListOfLinkMapsOrListOfArrayListOfLinkMaps)) {
			iterateOverLinkMaps(arrayListOfLinkMapsOrListOfArrayListOfLinkMaps, linkRelNameToValidate, actualLinkvalues);

		} else if (responseContainsMultipleRecords(arrayListOfLinkMapsOrListOfArrayListOfLinkMaps)) {
			for (int i = 0; i < arrayListOfLinkMapsOrListOfArrayListOfLinkMaps.size(); i++) {

				ArrayList listOfLinkMaps = (ArrayList) arrayListOfLinkMapsOrListOfArrayListOfLinkMaps.get(i);
				iterateOverLinkMaps(listOfLinkMaps, linkRelNameToValidate, actualLinkvalues);
			}
		}
	}

	private void iterateOverLinkMaps(List arrayListOfLinkMaps, String linkRelNameToValidate, Set<String> actualLinkvalues) {
		Iterator iterateOverListOfLinkMaps = arrayListOfLinkMaps.iterator();
		boolean linkFound = false;
		while (iterateOverListOfLinkMaps.hasNext() && !linkFound) {
			Map linksMap = (Map) iterateOverListOfLinkMaps.next();
			if (linkRelNameToValidate.equals(linksMap.get(LINK_REL))) {
				String linkHrefFromResponse = linksMap.get(LINK_HREF).toString();
				actualLinkvalues.add(linkHrefFromResponse);
				linkFound = true;
			}
		}
		Assert.assertTrue(linkFound);
	}

	private void validatePropertyArrayWithWildCard(List<String> responseValues, ArrayList<String> expectedValues) {
		int index = 0;
		for (String expectedValue : expectedValues) {
			if (expectedValue.startsWith(DataProviderBaseModel.Property.WILDCARD.toString())) {
				//If each element in response is represented as an Array, Then reading the element by accessing its zero index position.
				if (responseValues.toArray()[index] instanceof List) {
					List<String> response = (List<String>) responseValues.toArray()[index];
					assertThat(response.get(ZERO_INDEX), containsString(expectedValue.substring(1)));
				} else {
					assertThat(responseValues.get(index), containsString(expectedValue.substring(1)));
				}
			} else {
				if (responseValues.toArray()[index] instanceof List) {
					List<String> response = (List<String>) responseValues.toArray()[index];
					MatcherAssert.assertThat(response, everyItem(new CaseInsensitiveContainsMatcher(expectedValue)));
				} else {
					Assert.assertEquals(responseValues.get(index), expectedValue);
				}
			}
			index++;
		}
	}

	private void validatePropertyWithArrayResponseValues(ValidatableResponse response, String property, String valueToCheck) {

		List<String> responseValues = (List<String>) response.extract().body().jsonPath().get(property);
		if (valueToCheck.startsWith(DataProviderBaseModel.Property.WILDCARD.toString()) && valueToCheck.endsWith(DataProviderBaseModel.Property.WILDCARD.toString())) {
			validatePropertyWithContains(response, responseValues, valueToCheck.substring(1, valueToCheck.length() - 1));
		} else if (valueToCheck.startsWith(DataProviderBaseModel.Property.WILDCARD.toString())) {
			validatePropertyEndsWith(response, responseValues, valueToCheck.substring(1));
		} else if (valueToCheck.endsWith(DataProviderBaseModel.Property.WILDCARD.toString())) {
			validatePropertyStartsWith(response, responseValues, valueToCheck.substring(0, valueToCheck.length() - 1));
		}
	}

	private void validatePropertyWithStringResponse(ValidatableResponse response, String property, String valueToCheck) {

		if (valueToCheck.startsWith(DataProviderBaseModel.Property.WILDCARD.toString()) && valueToCheck.endsWith(DataProviderBaseModel.Property.WILDCARD.toString())) {
			validatePropertyWithContains(response, property, valueToCheck.substring(1, valueToCheck.length() - 1));
		} else if (valueToCheck.startsWith(DataProviderBaseModel.Property.WILDCARD.toString())) {
			validatePropertyEndsWith(response, property, valueToCheck.substring(1));
		} else if (valueToCheck.endsWith(DataProviderBaseModel.Property.WILDCARD.toString())) {
			validatePropertyStartsWith(response, property, valueToCheck.substring(0, valueToCheck.length() - 1));
		}
	}

	/**
	 * This method validates that value for a property in all records match expected value
	 * 
	 * @param response
	 *            : response from test method
	 * @param responseValues
	 *            : values coming from multiple records in response
	 * @param value
	 *            : expected value of property
	 */
	protected void validatePropertyWithContains(ValidatableResponse response, List<String> responseValues, String value) {
		assertThat(responseValues, everyItem(new CaseInsensitiveContainsMatcher(value)));
	}

	/**
	 * This method validates that value for a property should have contains value.
	 * 
	 * @param response
	 *            : response from test method
	 * @param property
	 *            : property to validate in response
	 * @param value
	 *            : expected value of property
	 */
	private void validatePropertyWithContains(ValidatableResponse response, String property, String value) {
		response.body(property, new CaseInsensitiveContainsMatcher(value));
	}

	/**
	 * This method validates that value for a property in all records starts with expected value
	 * 
	 * @param response
	 *            : response from test method
	 * @param responseValues
	 *            : values coming from multiple records in response
	 * @param value
	 *            : expected prefix value of property
	 */
	private void validatePropertyStartsWith(ValidatableResponse response, List<String> responseValues, String value) {
		MatcherAssert.assertThat(responseValues, everyItem(new CaseInsensitiveStartsWithMatcher(value)));
	}

	/**
	 * This method validates that value for a property should starts with value.
	 * 
	 * @param response
	 *            : response from test method
	 * @param property
	 *            : property to validate in response
	 * @param value
	 *            : expected value of property
	 */
	private void validatePropertyStartsWith(ValidatableResponse response, String property, String value) {
		response.body(property, new CaseInsensitiveStartsWithMatcher(value));
	}

	/**
	 * This method validates that value for a property in all records ends with expected value
	 * 
	 * @param response
	 *            : response from test method
	 * @param responseValues
	 *            : values coming from multiple records in response
	 * @param value
	 *            : expected suffix value of property
	 */
	private void validatePropertyEndsWith(ValidatableResponse response, List<String> responseValues, String value) {
		MatcherAssert.assertThat(responseValues, everyItem(new CaseInsensitiveEndsWithMatcher(value)));
	}

	/**
	 * This method validates that value for a property should ends with value.
	 * 
	 * @param response
	 *            : response from test method
	 * @param property
	 *            : property to validate in response
	 * @param value
	 *            : expected value of property
	 */
	private void validatePropertyEndsWith(ValidatableResponse response, String property, String value) {
		response.body(property, new CaseInsensitiveEndsWithMatcher(value));
	}

	/**
	 * Method to validate that all values for a property fits in the given array
	 * 
	 * @param response
	 *            : response from test method
	 * @param responseValues
	 *            : values coming from multiple records in response
	 * @param value
	 *            : Set of values which should exist in response values
	 */
	private void validateProperty(ValidatableResponse response, Set<String> responseValues, ArrayList<String> value) {
		assertThat(responseValues.toArray(), Matchers.arrayContainingInAnyOrder(value.toArray()));
	}

	/**
	 * Method to validate property value with expected value(String)
	 * 
	 * @param response
	 *            : response from test method
	 * @param property
	 *            : property to validate
	 * @param value
	 *            : expected value
	 */
	private void validateProperty(ValidatableResponse response, String property, String value) {
		response.body(property, equalTo(value));
	}

	/**
	 * Method to validate property value with expected value(Integer)
	 * 
	 * @param response
	 *            : response from test method
	 * @param property
	 *            : property to validate
	 * @param value
	 *            : expected value
	 */
	private void validateProperty(ValidatableResponse response, String property, Integer value) {
		response.body(property, equalTo(value));
	}

	/**
	 * Method to validate property value is greater than or equal to expected value(Integer)
	 * 
	 * @param response
	 *            : response from test method
	 * @param property
	 *            : property to validate
	 * @param value
	 *            : expected value
	 */
	private void validatePropertyGreaterThanEqual(ValidatableResponse response, String property, Integer value) {
		response.body(property, Matchers.greaterThanOrEqualTo(value));
	}

	/**
	 * Method to validate property value is less than or equal to expected value(Integer)
	 * 
	 * @param response
	 *            : response from test method
	 * @param property
	 *            : property to validate
	 * @param value
	 *            : expected value
	 */
	private void validatePropertyLessThanEqual(ValidatableResponse response, String property, Integer value) {
		response.body(property, Matchers.lessThanOrEqualTo(value));
	}

	/**
	 * @param response
	 * @param size
	 */
	protected void validateSize(ValidatableResponse response, Integer size) {
		if (size != DataProviderQueryStringModel.NO_RESPONSE_COUNT_VALUE) {
			response.body("data.list.size()", is(size));
		}
	}

	/**
	 * This method validates that all required attributes mentioned in data provider exist in response json
	 * 
	 * @param response
	 *            : Object which holds the JSON response
	 * @param requiredAttributes
	 *            : List of required attributes coming from data provider
	 */
	protected void validateRequiredAttribute(ValidatableResponse response, List<String> requiredAttributes) {
		for (String attribute : requiredAttributes) {
			response.body(attribute, not(equalTo(null)));
		}
	}

	/**
	 * This method validates the schema structure comign from REST
	 * 
	 * @param response
	 *            : JSON response from server
	 * @param expectedSchemaPath
	 *            : schema to validate with
	 */
	protected void validateSchema(ValidatableResponse response, String expectedSchemaPath) {
		if (StringUtils.isNotBlank(expectedSchemaPath)) {
			response.body(matchesJsonSchemaInClasspath(expectedSchemaPath));
		}
	}

	protected void validateXmlSchema(ValidatableResponse response, String expectedSchemaPath) {
		if (StringUtils.isNotBlank(expectedSchemaPath)) {
			response.body(io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath(expectedSchemaPath));
		}
	}

	/**
	 * This method validates all the properties with the data coming from REST
	 * 
	 * @param response
	 *            : JSON response from server
	 * @param propertyMapArray
	 *            : Array of maps with properties to validate
	 */
	protected void validateProperties(ValidatableResponse response,
			ArrayList<HashMap<String, Object>> propertyMapArray) {
		if (propertyMapArray != null) {
			for (HashMap<String, Object> propertyMap : propertyMapArray) {
				String property = (String) propertyMap.get(DataProviderBaseModel.Property.FILTER_ATTRIBUTE.toString());
				Object value = propertyMap.get(DataProviderBaseModel.Property.FILTER_VALUE.toString());
				String type = (String) propertyMap.get(DataProviderBaseModel.Property.FILTER_TYPE.toString());
				validateProperty(response, property, value, type);
			}
		}
	}

	protected void validateXmlProperties(ValidatableResponse response, ArrayList<HashMap<String, Object>> propertyMapArray) {

		if(propertyMapArray != null) {
			for(HashMap<String, Object> propertyMap : propertyMapArray) {

				Object value = propertyMap.get(DataProviderBaseModel.Property.FILTER_VALUE.toString());
				String xpath = (String) propertyMap.get(Property.XPATH.toString());
				validateXmlProperty(response,xpath,value);

			}
		}
	}

	private void validateXmlProperty(ValidatableResponse response, String xpath, Object value) {

		if(value instanceof Integer) {
			Integer val = Integer.parseInt(response.extract().body().xmlPath().get(xpath));
			Assert.assertEquals((Integer) value, val);
		} else if(value instanceof String) {
			Assert.assertEquals((String) value,response.extract().body().xmlPath().get(xpath));
		}
	}

	protected void validateTextProperties(ValidatableResponse response, ArrayList<HashMap<String, Object>> propertyMapArray) {

		if(propertyMapArray != null) {
			for(HashMap<String, Object> propertyMap : propertyMapArray) {
				String validateString = propertyMap.get(DataProviderBaseModel.Property.FILTER_ATTRIBUTE.toString()) + "=" + propertyMap.get(DataProviderBaseModel.Property.FILTER_VALUE.toString());
				validateTextProperty(response, validateString);
			}
		}
	}

	private void validateTextProperty(ValidatableResponse response, String value) {
		Assert.assertTrue(response.extract().asString().contains(value));
	}

	protected void validateLinks(ValidatableResponse response, ArrayList<HashMap<String, Object>> propertyMapList, Set<String> setOfAllTheLinkValuesToValidate, boolean checkForLinkExist) {
		Set<String> setOflinkRelNamesFromResponseToValidate = new HashSet();
		if (propertyMapList != null) {
			for (HashMap<String, Object> propertyMap : propertyMapList) {
				if (propertyMap.containsKey(DataProviderBaseModel.Property.LINK_NAME.toString())) {
					String linkRelNameToValidate = (String) propertyMap.get(DataProviderBaseModel.Property.LINK_NAME.toString());
					String filterAttribute = (String) propertyMap.get(DataProviderBaseModel.Property.FILTER_ATTRIBUTE.toString());
					extractLinksFromResponse(linkRelNameToValidate, response, filterAttribute, setOflinkRelNamesFromResponseToValidate);
				}
			}
			if (checkForLinkExist) {
				Assert.assertTrue(setOflinkRelNamesFromResponseToValidate.containsAll(setOfAllTheLinkValuesToValidate));
			}
			else {
				Assert.assertFalse(setOflinkRelNamesFromResponseToValidate.containsAll(setOfAllTheLinkValuesToValidate));
			}

		}
	}

	/**
	 * This method validates the status message coming from REST with the message coming from data provider
	 * 
	 * @param response
	 *            : JSON response from server
	 * @param status
	 *            : Status message coming from data provider
	 */
	protected void validateStatus(ValidatableResponse response, String status) {
		response.body("status", equalTo(status));
	}

	protected String URLEncodeApiQueryStringFilter(String queryString) {
		final String PREFIX_GROUP = "prefix";
		final String QS_GROUP = "qs";
		final String POSTFIX_GROUP = "postfix";
		final String QUERYSTRING_REGEX = "(?<" + PREFIX_GROUP + ">.*)(?<" + QS_GROUP + ">\\&?qs=)(?<" + POSTFIX_GROUP + ">.*)";
		final String URL_ENCODING = "UTF-8";

		Pattern qsRegex = Pattern.compile(QUERYSTRING_REGEX);
		Matcher matcher = qsRegex.matcher(queryString);
		if (matcher.find()) {
			try {
				String qs = URLEncoder.encode(matcher.group(POSTFIX_GROUP), URL_ENCODING);
				//utiltiy doesn't encode "*" so have to do it separately
				qs = qs.replace("*", "%2A");
				queryString = matcher.group(PREFIX_GROUP) + matcher.group(QS_GROUP) + qs;
			} catch (UnsupportedEncodingException e) {
				Assert.fail("Invalid Encoding!");
			}
		}

		return queryString;
	}

	/*
	 * Update the requestDataSet's filter value with the given value. It will
	 * formats the string, mentioned in Filter Value field of "Validate" block
	 * in request data set.
	 *
	 * @param requestDataSet : request data set
	 *
	 * @param valueToReplace : value which needs to be replaced with filter
	 * value
	 */
	protected void updateFilterValue(DataProviderQueryStringModel requestDataSet, String valueToReplace, String attrbuteToReplace) {
		if (requestDataSet.getPropertyMap() != null) {
			for (HashMap<String, Object> propertyMap : requestDataSet.getPropertyMap()) {
				if (propertyMap.containsKey(DataProviderBaseModel.Property.FILTER_REPLACEABLE_ATTRIBUTE.toString()) && propertyMap.containsKey(DataProviderBaseModel.Property.FILTER_VALUE.toString())) {
					Object currentValue = propertyMap.get(DataProviderBaseModel.Property.FILTER_VALUE.toString());
					String attribute = (String) propertyMap.get(DataProviderBaseModel.Property.FILTER_REPLACEABLE_ATTRIBUTE.toString());
					if (currentValue instanceof String && attribute.equals(attrbuteToReplace)) {
						String value = String.format(currentValue.toString(), valueToReplace);
						propertyMap.put(DataProviderBaseModel.Property.FILTER_VALUE.toString(), value);
					}
				}
			}
		}
	}

	/**
	 * This method return a basic auth token
	 * 
	 * @param username
	 *            : username of the user who wants to log in
	 * @param password
	 *            : password of the user who wants to log in
	 */
	protected String getBasicAuthToken(String username, String password) {
		final String basicAuthPrefix = "Basic ";
		String authCookie = (username + ":" + password);
		String basicAuthToken = new String(Base64.encode(authCookie.getBytes()));
		return basicAuthPrefix + basicAuthToken;
	}

	/**
	 * This method return a encoded string value
	 * 
	 * @param query
	 *            : value needs to be encoded
	 */
	protected String getEncodedString(String query) {
		if (query != null) {
			return URLEncoder.encode(query);
		}
		return null;
	}
}
