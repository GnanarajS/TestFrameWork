package com.hp.ipg.test.framework.rsapi.services.base;

import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import io.restassured.filter.log.LogDetail;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

/**
 * Base class for all Resource Services
 */
public abstract class ResourceServiceBase<T extends ResourceBase> {
	protected final Logger LOGGER;

	public ResourceServiceBase() {
		LOGGER = org.slf4j.LoggerFactory.getLogger(this.getClass());
	}

	public abstract String getEndpoint();

	public T create(T resource) {

		return given().
				contentType(resource.getContentType()).
				body(resource).
				when().log().ifValidationFails(LogDetail.ALL).
				post(getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).
				extract().body().as((Class<T>) resource.getClass());
	}
}
