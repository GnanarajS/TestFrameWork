package com.hp.ipg.test.framework.rsapi.services;

import static io.restassured.RestAssured.given;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.DeviceImage;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Component
public class DeviceImageService extends ResourceServiceBase{

	public static final String POST_DEVICE_IMAGE_ENDPOINT = "/devices";
	public final static String AUTHORIZATION = "Basic am9obi5zbWl0aEBocC5jb206MTIzNDU2Nzg=";
	@Autowired
	private TestConfiguration testConfiguration;

	@Override
	public String getEndpoint() {
		return POST_DEVICE_IMAGE_ENDPOINT;
	}

	public Response createDeviceImage(DeviceImage deviceImage) throws IOException {

		RequestSpecification request = given();
		ArrayList<DeviceImage> deviceImageRequest = new ArrayList<>();
		deviceImageRequest.add(deviceImage);
		return request.
				header(ResourceBase.AUTHORIZATION.toString(), AUTHORIZATION).
				header("Content-Type", DeviceImage.getContentType()).
				accept(DeviceImage.getContentType()).
				body(deviceImageRequest).
				when().log().ifValidationFails(LogDetail.ALL).
				post(testConfiguration.getPONUserBaseUrI() + getEndpoint()).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value()).extract().response();
	}
}
