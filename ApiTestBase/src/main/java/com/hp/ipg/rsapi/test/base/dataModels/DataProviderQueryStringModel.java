package com.hp.ipg.rsapi.test.base.dataModels;

import java.util.ArrayList;
import java.util.HashMap;

import com.hp.ipg.test.framework.rsapi.config.ApiVersion;

public class DataProviderQueryStringModel extends DataProviderBaseModel {
	public static final String QUERY_STRING_SEPARATOR = "?";
	public static final String EMPTY_STRING = "";
	public static final Integer NO_RESPONSE_COUNT_VALUE = -1;

	/**
	 * Get the request endpoint to test users resource for version 2
	 */
	public String getRequestEndpoint() {
		return (String) this.get(Property.REQUEST_END_POINT.toString());
	}

	/**
	 * Get query string to filter the records or add paging
	 * 
	 * @return
	 */
	public String getQueryString() {
		return (String) this.get(Property.QUERY_STRING.toString());
	}

	public String getLinkName() {
		return (String) this.get(Property.LINK_NAME.toString());
	}

	/**
	 * Get property map of json response attributes and values to validate
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getPropertyMap() {
		ArrayList<HashMap<String, Object>> validationProperties = (ArrayList<HashMap<String, Object>>) this.get(Property.VALIDATE.toString());
		return validationProperties;
	}

	/**
	 * Get property map of json response attributes and values to validate Link
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getPropertyMapToValidateLink() {
		ArrayList<HashMap<String, Object>> validationProperties = (ArrayList<HashMap<String, Object>>) this.get(Property.VALIDATE_LINK.toString());
		return validationProperties;
	}

	/**
	 * Get the value for expected number of records in response
	 * 
	 * @return
	 */
	public Integer getResponseSize() {
		HashMap<String, Object> resBody = getResponseBody();
		if (resBody != null) {
			if (resBody.containsKey(Property.RESPONSE_COUNT.toString())) {
				return Integer.parseInt((String) getResponseBody().get(Property.RESPONSE_COUNT.toString()));
			}
		}

		return NO_RESPONSE_COUNT_VALUE;
	}

	/* (non-Javadoc)
	 * @see test.com.hp.ipg.rsapi.portal.dataModels.base.DataProviderBaseModel#getVersion()
	 */
	@Override
	protected ApiVersion getVersion() {
		return ApiVersion.DEFAULT;
	}

	/**
	 * Get the paging link to test pagination.
	 */
	public String getPagingLink() {
		return (String) this.get(Property.PAGING_LINK.toString());
	}

	/**
	 * This method returns expected size from the paging link
	 */
	public Integer getPagingResponseSize() {
		if (this.containsKey(Property.PAGING_RESPONSE_SIZE.toString())) {
			return Integer.parseInt((String) this.get(Property.PAGING_RESPONSE_SIZE.toString()));
		}
		return NO_RESPONSE_COUNT_VALUE;
	}

	/**
	 * This method returns
	 */
	public ArrayList<HashMap<String, Object>> getPagingPropertyMap() {
		return (ArrayList<HashMap<String, Object>>) this.get(Property.PAGING_PROPERTY_MAP.toString());
	}
}
