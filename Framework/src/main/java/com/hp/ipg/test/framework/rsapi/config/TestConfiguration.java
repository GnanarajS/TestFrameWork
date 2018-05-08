package com.hp.ipg.test.framework.rsapi.config;

import com.hp.ipg.test.framework.genericLib.testExecution.TestSuiteBase;
import com.hp.ipg.test.framework.genericLib.testExecution.utils.BuildUtils;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

@Configuration
@ComponentScan(basePackages = { "com.hp.ipg.test.framework.rsapi", "com.hp.ipg.test.framework.genericLib" })
public class TestConfiguration {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TestConfiguration.class);

	@Configuration
	@PropertySource("classpath:properties/apiTest/qa.properties")
	@Profile({ "qa" })
	static class QA {
	}

	@Configuration
	@PropertySource("classpath:properties/apiTest/dev.properties")
	@Profile({ "dev" })
	static class Dev {
	}

	@Value("${baseUri}")
	private String baseUri;

	@Value("${PDHBaseUrl}")
	private String PDHBaseUrl;

	@Value("${lookUpBaseURI}")
	private String lookUpBaseURI;

	@Value("${jobDestination.success}")
	private String jobSuccess;

	@Value("${jobDestination.failure}")
	private String jobFail;

	@Value("${hpRoamPool.destination}")
	private String poolDestination;

	@Value("${PDHUserName}")
	private String PDHUserName;

	@Value("${PDHPassword}")
	private String PDHPassword;

	@Value("${client_id}")
	private String clientID;

	@Value("${client_secret}")
	private String clientSecret;

	@Value("${PONUserBaseUrI}")
	private String PONUserBaseUrI;

	@PostConstruct
	public void InitializeRestAssured() {
		RestAssured.baseURI = baseUri;
		RestAssured.useRelaxedHTTPSValidation();

		try {
			PrintStream fileOutputStream = new PrintStream(new File(BuildUtils.getTestOutputDirectory(), TestSuiteBase.restAssuredLogFile));
			RestAssured.config = RestAssuredConfig.config().logConfig(new LogConfig().defaultStream(fileOutputStream));
		} catch (FileNotFoundException e) {
			LOGGER.error("Encountered file not found exception", e);
		}
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		pspc.setLocalOverride(true);
		return pspc;
	}
	
	public String getBaseUri() {
		return baseUri;
	}
	
	public String getPDHBaseUrl() {
		return PDHBaseUrl;
	}

	public String getLookUpBaseURI() {
		return lookUpBaseURI;
	}

	public String getJobSuccessDestination(){
		return jobSuccess;
	}

	public String getJobFailureDestination(){
		return jobFail;
	}

	public String getPoolDestination(){
		return poolDestination;
	}

	public String getPDHUserName(){
		return PDHUserName;
	}
	public String getPDHPassword(){
		return PDHPassword;
	}
	public String getClientID(){
		return clientID;
	}
	public String getClientSecret(){
		return clientSecret;
	}

	public String getPONUserBaseUrI(){
		return PONUserBaseUrI;
	}
}
