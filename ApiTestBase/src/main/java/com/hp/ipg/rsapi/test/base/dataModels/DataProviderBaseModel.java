package com.hp.ipg.rsapi.test.base.dataModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;
import com.hp.ipg.test.framework.rsapi.config.ResourceName;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;

/**
 * This abstract base class provides methods to access general attributes loaded
 * via data provider.
 */
public abstract class DataProviderBaseModel extends HashMap<String, Object> {

	public enum Property {
		ID("Id"),
		ENABLED("Enabled"),
		DESCRIPTION("Description"),
		CONTENT_TYPE("ContentType"),
		REQUEST_END_POINT("RequestEndPoint"),
		REQUEST_DATA("RequestData"),
		REQUEST_DATA_RAW("RequestDataRaw"),
		CLEAR_DATA("ClearData"),
		COUNT("Count"),
		EXPECTED_STATUS_CODE("ExpectedStatusCode"),
		EXPECTED_STATUS_CODES("ExpectedStatusCodes"),
		LOGIN_CONTEXT("LoginContext"),
		IS_LICENSED("IsLicensed"),
		IS_FREE("IsFree"),
		RESPONSE_BODY("ResponseBody"),
		STATUS_MESSAGE("StatusMessage"),
		REQUIRED_ATTRIBUTES("RequiredAttributes"),
		RESPONSE_SCHEMA_PATH("Schema"),
		RESPONSE_COUNT("Count"),
		RESOURCE_SET("ResourceSet"),
		QUERY_STRING("QueryString"),
		VALIDATE("Validate"),
		VALIDATE_LINK("ValidateLink"),
		FILTER_ATTRIBUTE("FilterAttribute"),
		FILTER_VALUE("FilterValue"),
		LINK_NAME("LinkName"),
		XPATH("Xpath"),
		USER_TYPE("UserType"),
		REGION("region"),
		SERVICE_AGREEMENT_ID("ServiceAgreementID"),
		PRODUCT_TYPE("ProductType"),
		FILTER_TYPE("FilterType"),
		WILDCARD("*"),
		UUID("UUID"),
		PAGING_LINK("PagingLink"),
		PAGING_RESPONSE_SIZE("PagingResponseSize"),
		PAGING_PROPERTY_MAP("PagingValidate"),
		FILTER_REPLACEABLE_ATTRIBUTE("FilterReplaceableAttribute"),
		ACCOUNT_ID("AccountId"),
		PROPERTY_MATCHER("propertyMatcher"),
		EXPIRED_SAML_TOKEN("ExpiredSAMLToken"),
		GREATER_THAN_OR_EQUALTO("greaterThanOrEqualTo"),
		LESS_THAN_OR_EQUALTO("lessThanOrEqualTo"),
		USERNAME("username"),
		PASSWORD("password"),
		FORM_URL_CONTENT_TYPE("application/x-www-form-urlencoded"),
		FORM_CONTENT_TYPE("multipart/form-data"),
		TEXT_CONTENT_TYPE("text/plain; charset=UTF-8"),
		AUTHORIZATION("Authorization"),
		DOC_API_FUNC("docAPIfunc"),
 		CLIENT_SWPROTOCOL("clientSWProtocol"),
 		CLIENT_SWKEY("clientSWKey"),
 		CLIENT_SWNAME("clientSWName"),
 		CLIENT_SWVER("clientSWVer"),
 		USER_LANG("userLang"),
		JOB_REFERENCE_ID("jobReferenceID"),
 		JOB_DESTINATION("jobDestination"),
 		DOCUMENT_URL("documentURI"),
 		PO_COLOR("poColor"),
 		DOC_FUNC_TEST("Test_Doc_Func"),
 		BEARER("Bearer "),
		IS_SPECIAL_CHARACTER_OR_INTEGER("IsSpecialCharacterOrInteger"),
 		ISVALID_DOC_PROCESS("IsValidDocProcess"),
 		ISVALID_JOB_DESTINATION("IsValidJobDestination"),
		ISVALID_CLIENT_KEYS("IsValidClientKeys"),
		STATUS_FILTER("StatusFilter"),
		ISVALID_JOB_REFERENCE_ID("IsValidJobReferenceId"),
		GEO_LOCATION("GeoLocation"),
		IS_VALID_GEOLOCATION("IsValidGeoLocation"),
		OPERATION("operation"),
		OPERATOR("Operator"),
		IS_EMPTY_FAMILY_NAME("IsEmptyFamilyName"),
		FAMILY_NAME("FamilyName"),
		GIVEN_NAME("GivenName"),
		RELEASE_CODE("releaseCode"),
		USER_TOKEN("Token");

		private String name;

		Property(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	/**
	 * - * Gets the specified content type value
	 * - *
	 * - * @return - specified content type or null
	 * -
	 */
	public String getContentType() {
		return (String) this.get(Property.CONTENT_TYPE.toString());
	}

	public String getDocApiFunc() {
		       return (String) this.get(Property.DOC_API_FUNC.toString());
	}
	/**
	 * Gets the specified ID of test
	 *
	 * @return - Id string value if it exists or null
	 */
	public String getId() {
		return (String) this.get(Property.ID.toString());
	}

	public String getAccountID() {
		return (String) this.get(Property.ACCOUNT_ID.toString());
	}

	/**
	 * Get the enabled state of the test case
	 *
	 * @return - boolean enabled state
	 */
	public boolean getEnabled() {
		if (this.containsKey(Property.ENABLED.toString()))
			return (boolean) this.get(Property.ENABLED.toString());

		return true;
	}

	/**
	 * Get the isFree value loaded from the data provider
	 *
	 * @return - boolean enabled state
	 */
	public boolean getIsFree() {
		if (this.containsKey(Property.IS_FREE.toString()))
			return (boolean) this.get(Property.IS_FREE.toString());

		return false;
	}

	/**
	 * Get the test description loaded from the data provider.
	 */
	public String getDescription() {
		return (String) this.get(Property.DESCRIPTION.toString());
	}

	/**
	 * Get the user type from the data provider.
	 */
	public String getUserType() {
		return (String) this.get(Property.USER_TYPE.toString());
	}

	/**
	 * Get the region loaded from the data provider.
	 */
	public String getRegion() {
		return (String) this.get(Property.REGION.toString());
	}

	/**
	 * Get the service agreement Id loaded from the data provider.
	 */
	public String getServiceAgreementId() {
		return (String) this.get(Property.SERVICE_AGREEMENT_ID.toString());
	}

	/**
	 * Get the product type loaded from the data provider.
	 */
	public String getProductType() {
		return (String) this.get(Property.PRODUCT_TYPE.toString());
	}

	/**
	 * Get the status code loaded from the data provider
	 */
	public Integer getExpectedStatusCode() {
		return Integer.parseInt((String) this.get(Property.EXPECTED_STATUS_CODE.toString()));
	}

	/**
	 * Get the Count loaded from the data provider
	 */
	public int getCount() {
		return (int) this.get(Property.COUNT.toString());
	}

	public ArrayList<Integer> getExpectedStatusCodes() {
		return (ArrayList<Integer>) this.get(Property.EXPECTED_STATUS_CODES.toString());
	}

	/**
	 * Get the logincontext loaded from the data provider.
	 */
	public String getLoginContext() {
		return (String) this.get(Property.LOGIN_CONTEXT.toString());
	}

	/**
	 * Get the DefaultEntitlementPolicy loaded from the data provider.
	 */

	/**
	 * Get the IsLicensed loaded from the data provider.
	 */
	public boolean isLicensed() {
		return (boolean) this.get(Property.IS_LICENSED.toString());
	}

	/**
	 * Get the response body loaded from the data provider.
	 */
	public HashMap<String, Object> getResponseBody() {
		return (HashMap<String, Object>) this.get(Property.RESPONSE_BODY.toString());
	}

	/**
	 * Get the status message loaded from the data provider.
	 */
	public String getStatusMessage() {
		return (String) this.get(Property.STATUS_MESSAGE.toString());
	}

	/**
	 * Get the required attributes which are suppose to come in response JSON
	 */
	public List<String> getRequiredAttributes() {
		Object attribute = this.get(Property.REQUIRED_ATTRIBUTES.toString());
		if (attribute != null && attribute instanceof List<?>) {
			return (List<String>) attribute;
		}
		return null;
	}

	/**
	 * Gets the RequestData containing model property overrides
	 *
	 * @return - property hashmap data
	 */
	public HashMap<String, Object> getRequestData() {
		return (HashMap<String, Object>) this.get(Property.REQUEST_DATA.toString());
	}

	/**
	 * Gets the RequestData as a raw string
	 *
	 * @return - request data string
	 */
	public String getRequestDataRaw() {
		return (String) this.get(Property.REQUEST_DATA_RAW.toString());
	}

	/**
	 * Gets the UUID as a raw string
	 *
	 * @return - UUID string
	 */
	public String getUuid() {
		return (String) this.get(Property.UUID.toString());
	}

	/**
	 * Gets the expired SAML token as a raw string
	 *
	 * @return - expired token string
	 */
	public String getExpiredSAMLToken() {
		return (String) this.get(Property.EXPIRED_SAML_TOKEN.toString());
	}

	/**
	 * Get the username loaded from the data provider.
	 */
	public String getUserName() {
		return (String) this.get(Property.USERNAME.toString());
	}

	/**
	 * Get the password loaded from the data provider.
	 */
	public String getPassWord() {
		return (String) this.get(Property.PASSWORD.toString());
	}

	/**
	 * Gets the list of properties to clear from model
	 *
	 * @return - array of properties to clear from model
	 */
	public String[] getClearData() {
		ArrayList<String> data = (ArrayList<String>) this.get(Property.CLEAR_DATA.toString());
		if (data != null) {
			return data.toArray(new String[data.size()]);
		}

		return new String[0];
	}

	/**
	 * Updates the model with data provider data
	 *
	 * @param model
	 *            - model data
	 * @return - updated model data
	 */
	public ResourceBase updateModel(ResourceBase model) {
		if (model == null) {
			return model;
		}

		String[] clearData = getClearData();
		if (clearData != null) {
			for (String property : clearData) {
				model.remove(property);
			}
		}

		HashMap<String, Object> requestData = getRequestData();
		if (requestData != null) {
			model.putAll(getRequestData());
		}

		return model;
	}

	/**
	 * Method gets version information for instance
	 *
	 * @return
	 */
	protected ApiVersion getVersion(){
		return ApiVersion.DEFAULT;
	}

	/**
	 * Method gets request endpoint which will be different for different dataModel
	 *
	 * @return
	 */
	public abstract String getRequestEndpoint();

	/**
	 * Get the schema path from the framework based on api version being tested.
	 */
	public String getResponseSchemaPath() {
		HashMap<String, Object> responseBody = getResponseBody();
		if (responseBody != null && responseBody.get(Property.RESPONSE_SCHEMA_PATH.toString()) != null) {
			String responseSchemaPath = ResourceBase.getSchemaPath(ResourceName.fromString((String) responseBody.get(Property.RESPONSE_SCHEMA_PATH.toString())), getVersion());
			Assert.assertTrue(responseSchemaPath != null, "Schema file not found for " + (String) getResponseBody().get(Property.RESPONSE_SCHEMA_PATH.toString()));
			return responseSchemaPath;
		}
		return null;
	}

	public String getXmlResponseSchemaPath() {
		HashMap<String, Object> responseBody = getResponseBody();
		if(responseBody != null && responseBody.get(Property.RESPONSE_SCHEMA_PATH.toString()) != null) {
			String responseSchemaPath = ResourceBase.getXmlSchemaPath(ResourceName.fromString((String) responseBody.get(Property.RESPONSE_SCHEMA_PATH.toString())), getVersion());
			Assert.assertTrue(responseSchemaPath != null, "Schema file not found for" + (String) getResponseBody().get(Property.RESPONSE_SCHEMA_PATH.toString()));
			return responseSchemaPath;
		}
		return null;
	}

	public String getReleaseCode() {
		return (String) this.get(Property.RELEASE_CODE.toString());
	}

	public String getJobOperation() {
		return (String) this.get(Property.OPERATION.toString());
	}

	public boolean isSpecialCharacterOrInteger() {
		return (boolean) this.get(Property.IS_SPECIAL_CHARACTER_OR_INTEGER.toString());
	}

	public String getUserToken() {
		return (String) this.get(Property.USER_TOKEN.toString());
	}
}
