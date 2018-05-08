package com.hp.ipg.test.framework.rsapi.services;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.ReleaseJob;
import com.hp.ipg.test.framework.rsapi.resources.User;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

@Component
public class JobService extends ResourceServiceBase {
    private static final String JOB_ACTION_ENDPOINT = "/cps/jobs/action";
    private static final String JOBS_API_ENDPOIN= "/cps/jobs";
    private static final String GET_JOBS_BY_REF_ID_ENDPOINT = "/cps/jobsByRefID/";
    private static final String RELEASE_JOB = "release-job";
    private static final String DELETE_JOB = "delete-job";

    @Autowired
    TestConfiguration testConfiguration;

    @Autowired
    protected UserService userService;

    @Override
    public String getEndpoint() {
        return JOB_ACTION_ENDPOINT;
    }

    public ValidatableResponse releaseJob(String token, String referenceID, String releaseCode, Boolean status) throws IOException {
        RequestSpecification request = given();
        request.header(ResourceBase.AUTHORIZATION, token);

        String destination = testConfiguration.getJobFailureDestination();
        if(status){
            destination = testConfiguration.getJobSuccessDestination();
        }

        ValidatableResponse response = request.
                formParam(ReleaseJob.Property.operation.toString(), RELEASE_JOB).
                formParam(ReleaseJob.Property.jobReferenceID.toString(), referenceID).
                formParam(ReleaseJob.Property.outputDestinationID.toString(), destination).
                formParam(ReleaseJob.Property.releaseCode.toString(), releaseCode).
                when().log().ifValidationFails().
                post(getEndpoint()).
                then().log().ifValidationFails().
                statusCode(HttpStatus.OK.value());

        return response;
    }
    public ValidatableResponse deleteJob(String token, String referenceID, String releaseCode, Boolean status) throws IOException {
        RequestSpecification request = given();
        request.header(ResourceBase.AUTHORIZATION, token);

        String destination = testConfiguration.getJobFailureDestination();
        if(status){
            destination = testConfiguration.getJobSuccessDestination();
        }

        ValidatableResponse response = request.
                formParam(ReleaseJob.Property.operation.toString(), DELETE_JOB).
                formParam(ReleaseJob.Property.jobReferenceID.toString(), referenceID).
                formParam(ReleaseJob.Property.outputDestinationID.toString(), destination).
                formParam(ReleaseJob.Property.releaseCode.toString(), releaseCode).
                when().log().ifValidationFails(LogDetail.ALL).
                post(getEndpoint()).
                then().log().ifValidationFails(LogDetail.ALL).
                statusCode(HttpStatus.OK.value());

        return response;
    }


    public void deleteAllJobs(String userName) throws IOException {

        RequestSpecification request = given();

        String token =userService.getBearerToken(userName);

        Response response= given().
                header(ResourceBase.AUTHORIZATION.toString(), token).
                accept(User.getContentType()).
                when().log().ifValidationFails(LogDetail.ALL).
                get(JOBS_API_ENDPOIN).
                then().log().ifValidationFails(LogDetail.ALL).
                statusCode(HttpStatus.OK.value()).extract().response();

        ArrayList jobList = response.getBody().jsonPath().get("jobs");

        for(int i=0;i<jobList.size();i++) {
            HashMap jobData = ((HashMap) jobList.get(i));

            String jobReferenceId = jobData.get("jobReferenceId").toString();

            String releaseCode = (String) jobData.get("releaseCode");

            deleteJob(token,jobReferenceId,releaseCode,true);
        }
    }
    
    public ValidatableResponse getJobByJobRefId(String token, String jobRefId) throws IOException {
    	
		RequestSpecification request = given();

		return request.
				header(ResourceBase.AUTHORIZATION, token).
				when().log().ifValidationFails(LogDetail.ALL).
				get(GET_JOBS_BY_REF_ID_ENDPOINT+jobRefId).
				then().log().ifValidationFails(LogDetail.ALL).
				statusCode(HttpStatus.OK.value());
	}
}
