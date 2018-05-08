package com.hp.ipg.test.framework.rsapi.services;

import com.hp.ipg.test.framework.rsapi.config.TestConfiguration;
import com.hp.ipg.test.framework.rsapi.resources.DocStatus;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;

import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.io.IOException;
import static io.restassured.RestAssured.given;

@Component
public class DocStatusService extends ResourceServiceBase {

    public static String JOB_PATH = "DocProcess.jobReferenceID";
    private static final String JOB_COMPLETE = "DocProcess.jobComplete";
    private static final String SUCCESS = "SUCCESS";

    @Autowired
    DocApiService docApiService;

    @Autowired
    protected TestConfiguration testConfiguration;

    @Autowired
    UserService userService;

    @Override
    public String getEndpoint() {
        return docApiService.getEndpoint();
    }


    public void createJobAndCheckStatus(String url, String userName) throws IOException {
        RequestSpecification request = given();

        String token = userService.getBearerToken(userName);
        ValidatableResponse createJob= docApiService.createJob(url, userName);

        String jobId= createJob.extract().body().xmlPath().get(JOB_PATH);
        ValidatableResponse response=checkStatus(token,jobId) ;

        while (!(response.extract().body().xmlPath().get(JOB_COMPLETE).equals(SUCCESS))){
            response =  checkStatus(token,jobId);
        }

        Assert.assertTrue(response.extract().body().xmlPath().get(JOB_COMPLETE).equals(SUCCESS), "Job status is pending");
    }

    public ValidatableResponse checkStatus(String token, String jobId) throws IOException {
        RequestSpecification request = given();

        DocStatus docStatus = DocStatus.getInstance();

        return given().
                header(ResourceBase.AUTHORIZATION.toString(), token).
                formParam(DocStatus.Property.docAPIfunc.toString(), docStatus.getDocAPIfunc()).
                formParam(DocStatus.Property.clientSWProtocol.toString(), docStatus.getClientSWProtocol()).
                formParam(DocStatus.Property.clientSWKey.toString(), docStatus.getClientSWKey()).
                formParam(DocStatus.Property.clientSWName.toString(), docStatus.getClientSWName()).
                formParam(DocStatus.Property.clientSWVer.toString(), docStatus.getClientSWVer()).
                formParam(DocStatus.Property.jobReferenceID.toString(), jobId).
                formParam(DocStatus.Property.jobDestination.toString(), testConfiguration.getPoolDestination()).
                multiPart("", "").
                when().log().ifValidationFails(LogDetail.ALL).
                post(docApiService.getEndpoint()).
                then().log().ifValidationFails(LogDetail.ALL).
                statusCode(HttpStatus.OK.value());
    }
}
