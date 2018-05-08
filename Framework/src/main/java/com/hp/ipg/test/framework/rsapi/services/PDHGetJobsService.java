package com.hp.ipg.test.framework.rsapi.services;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.PDHGetJobs;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@Component
public class PDHGetJobsService extends ResourceServiceBase {

	@Autowired
	TestConfiguration testConfiguration;

	private PDHGetJobs pdhGetJob;

	private String numOfJobsPath = "GetJobs.numJobs";

	private String jobInfoPath = "GetJobs.JobInfo";

	@Override
	public String getEndpoint() {
		return testConfiguration.getPDHBaseUrl();
	}

	public String getAttributeValueOfGetJob(String attributeId) throws IOException {

		RequestSpecification request = given();

		pdhGetJob = PDHGetJobs.getInstance();

		ValidatableResponse response = request
				.formParam(PDHGetJobs.Property.FcsAPIfunc.toString(), pdhGetJob.getFcsAPIfunc())
				.formParam(PDHGetJobs.Property.Password.toString(), testConfiguration.getPDHPassword())
				.formParam(PDHGetJobs.Property.PrinterURI.toString(), testConfiguration.getPoolDestination())
				.formParam(PDHGetJobs.Property.WhichJobs.toString(), pdhGetJob.getWhichJobs())
				.when().log().ifValidationFails()
				.post(getEndpoint())
				.then().log().ifValidationFails()
				.statusCode(HttpStatus.OK.value());

		String value = null;
		int noOfRecords = Integer.parseInt(response.extract().body().xmlPath().get(numOfJobsPath).toString());

		//to get the value of jobId or PTId
		for (int i = 0; i < noOfRecords; i++) {
			value = response.extract().body().xmlPath().get(jobInfoPath + "[" + i + "]." + attributeId).toString();
			if (!value.isEmpty()) {
				break;
			}
		}

		return value;
	}
}
