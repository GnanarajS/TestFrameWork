package com.hp.ipg.test.framework.rsapi.services;

import com.hp.ipg.test.framework.rsapi.resources.DirSearch;
import com.hp.ipg.test.framework.rsapi.resources.base.ResourceBase;
import com.hp.ipg.test.framework.rsapi.services.base.ResourceServiceBase;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;
@Component
public class DirSearchService  extends ResourceServiceBase {

    public static final String DIR_SEARCH_ENDPOINT = "/cps/DirSearch";

    public static final String GET_DIR_SEARCH_CREATE = "/cps/DirSearch?ponAPIfunc=%s&clientSWKey=%s&clientSWName=%s&clientSWVer=%s&showChildren=1";

    @Override
    public String getEndpoint() {
        return GET_DIR_SEARCH_CREATE;
    }

    public ValidatableResponse getDirSearch(DirSearch dirSearch, String token){
        String queryUrl = String.format(GET_DIR_SEARCH_CREATE,
                dirSearch.getPonAPIfunc(),
                dirSearch.getClientSWKey(),
                dirSearch.getClientSWName(),
                dirSearch.getClientSWVer());
        return given().
                header(ResourceBase.AUTHORIZATION, token).
                when().log().ifValidationFails(LogDetail.ALL).
                post(queryUrl).
                then().log().ifValidationFails(LogDetail.ALL).
                statusCode(HttpStatus.OK.value());
    }
}
