package com.hp.ipg.test.framework.webApp.testExecution.clients;

import com.hp.ipg.test.framework.genericLib.testExecution.utils.BuildUtils;
import com.hp.ipg.test.framework.webApp.testExecution.config.SeleniumConfiguration;
import com.saucelabs.saucerest.SauceREST;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.ITestResult;
import org.testng.internal.TestResult;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSauceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSauceClient.class);
    private static final String SAUCE_HUB_URL = "http://%s:%s@ondemand.saucelabs.com:80/wd/hub";
    private static final String SAUCE_USER_FIELD = "SAUCE_USER_NAME";
    private static final String SAUCE_API_KEY_FIELD = "SAUCE_API_KEY";
    private static final String DEFAULT_USER = "anonymous";
    private static final String DEFAULT_ACCESS_KEY = "0e696160-8a84-4310-8579-e12140b4fddc";
    public static final String JOB_ID_ATTR_NAME = "sauceJobId";

    private String userName;
    private String accessKey;

    @Value("${grid.recordVideo:true}")
    private boolean recordVideo;
    @Value("${grid.recordScreenshots:true}")
    private boolean recordScreenshots;
    @Value("${grid.recordHtml:false}")
    private boolean recordHtml;
    @Value("${grid.maxDuration:1800}")
    private int maxDurationSeconds;
    @Value("${grid.commandTimeout:300}")
    private int commandTimeoutSeconds;
    @Value("${grid.idleTimeout:600}")
    private int idleTimeoutSeconds;

    @PostConstruct
    protected void init() {
        userName = getUser();
        accessKey = getSauceApiKey();

    }

    public URL getHubUrl() {
        try {
            LOGGER.info("Using Sauce hub URL [{}]...", SAUCE_HUB_URL);
            return new URL(String.format(SAUCE_HUB_URL, userName, accessKey));
        } catch (MalformedURLException e) {
            String exceptionMessage = String.format("Hub URL is invalid: [%s]. inner exception: [%s: %s]",
                    SAUCE_HUB_URL, e.getClass().getName(), e.getMessage());
            throw new RuntimeException(exceptionMessage);
        }
    }

    protected boolean useSauce() {
        return true;
    }

    protected boolean getRecordVideo() {
        return this.recordVideo;
    }

    protected boolean getRecordScreenshots() {
        return this.recordScreenshots;
    }

    protected boolean getRecordHtml() {
        return this.recordHtml;
    }

    protected int getMaxDurationSeconds() {
        return this.maxDurationSeconds;
    }

    protected int getCommandTimeoutSeconds() {
        return commandTimeoutSeconds;
    }

    protected int getIdleTimeoutSeconds() {
        return idleTimeoutSeconds;
    }



    public static String getUser() {
        return BuildUtils.getPropertyOrEnvVar(SAUCE_USER_FIELD, DEFAULT_USER);
    }

    public static String getSauceApiKey() {
        return BuildUtils.getPropertyOrEnvVar(SAUCE_API_KEY_FIELD, DEFAULT_ACCESS_KEY);
    }

    public DesiredCapabilities getCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities = this.sauceCaps(capabilities);
        capabilities.setCapability("sauce-advisor", false);
        return capabilities;
    }

    private DesiredCapabilities sauceCaps(DesiredCapabilities capabilities) {
        capabilities.setCapability("browserName", SeleniumConfiguration.getBrowser());
        capabilities.setCapability("platform", SeleniumConfiguration.getPlatform());
        capabilities.setCapability("sauce-advisor", false);
        capabilities.setCapability("record-video", getRecordVideo());
        capabilities.setCapability("video-upload-on-pass", false);
        capabilities.setCapability("capture-html", getRecordHtml());
        capabilities.setCapability("record-screenshots", getRecordScreenshots());
        capabilities.setCapability("idle-timeout", getIdleTimeoutSeconds());
        capabilities.setCapability("command-timeout", getCommandTimeoutSeconds());
        capabilities.setCapability("max-duration", getMaxDurationSeconds());
        return capabilities;
    }

    /**
     * Reports the job pass/fail to sauceGrid
     *
     * @param result
     *            the test result for the running test
     */
    public void reportResult(ITestResult result) throws IOException, JSONException {
        SessionId jobId = (SessionId) result.getAttribute(JOB_ID_ATTR_NAME);
        if (jobId != null) {
            // NOTE: This log statement format is very important.  The sauce jenkins plugin looks for this string in console output to link jobs to test names.
            LOGGER.info(String.format("SauceOnDemandSessionID=%s job-name=%s", jobId.toString(), result.getName()));

            Map<String, Object> updates = new HashMap<String, Object>();
            if (result.getStatus() == TestResult.SUCCESS) {
                updates.put("passed", true);
            } else if (result.getStatus() == TestResult.FAILURE) {
                updates.put("passed", false);
                if (result.getThrowable() != null) {
                    JSONObject customJson = new JSONObject();
                    customJson.put("exceptionMessage", result.getThrowable().getMessage());
                    updates.put("custom-data", customJson);
                }
            }

            updates.put("name", result.getMethod().getTestClass().getName() + "." + result.getName());
            updates.put("tags", Arrays.asList(result.getMethod().getGroups()));
            updateJobInfo(jobId, updates);
        }
    }

    /**
     * Uses Sauce REST API to post any key-value pairs to the job/test.
     *
     * @param jobId
     *            The SessionId obtained from RemoteWebDriver
     * @param updates
     *            KeyValue pairs for items to post to the Sauce job.
     * @throws IOException
     */
    private void updateJobInfo(SessionId jobId, Map<String, Object> updates) throws IOException {
        SauceREST rest = new SauceREST(userName, accessKey);
        rest.updateJobInfo(jobId.toString(), updates);
    }
}
