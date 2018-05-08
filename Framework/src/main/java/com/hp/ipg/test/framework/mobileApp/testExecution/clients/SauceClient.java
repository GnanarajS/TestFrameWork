package com.hp.ipg.test.framework.mobileApp.testExecution.clients;

import com.hp.ipg.test.framework.genericLib.testExecution.utils.BuildUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class SauceClient extends AppiumGridClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(SauceClient.class);
    private static final String SAUCE_HUB_URL = "https://us1.appium.testobject.com/wd/hub";

    @Value("${grid.resolution:}")
    private String resolution;
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
    @Value("${grid.deviceOrientation:portrait}")
    private String deviceOrientation;

    @Override
    public URL getHubUrl() {
        try {
            LOGGER.info("Using [{}] hub [{}]...", GridType.SAUCELABS.toString(), SAUCE_HUB_URL);
            return new URL(SAUCE_HUB_URL);
        } catch (MalformedURLException e) {
            String exceptionMessage = String.format("Hub URL is invalid: [%s]. inner exception: [%s: %s]",
                    SAUCE_HUB_URL, e.getClass().getName(), e.getMessage());
            throw new RuntimeException(exceptionMessage);
        }
    }

    @Override
    protected void setGridType() {
        super.gridType = GridType.SAUCELABS;
    }

    @Override
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

    protected String getDeviceOrientation() {
        return deviceOrientation;
    }

    @Override
    public DesiredCapabilities getiOSCapabilities(String bundleId) {
        DesiredCapabilities capabilities = super.getiOSCapabilities(bundleId);
        capabilities = sauceCaps(capabilities);
        return capabilities;
    }

    @Override
    public DesiredCapabilities getAndroidCapabilities(String appPackage, String appActivity) {
        DesiredCapabilities capabilities = super.getAndroidCapabilities(appPackage, appActivity);
        capabilities = sauceCaps(capabilities);
        return capabilities;
    }

    private DesiredCapabilities sauceCaps(DesiredCapabilities capabilities) {
        capabilities.setCapability("sauce-advisor", false);
        capabilities.setCapability("record-video", getRecordVideo());
        capabilities.setCapability("video-upload-on-pass", false);
        capabilities.setCapability("capture-html", getRecordHtml());
        capabilities.setCapability("record-screenshots", getRecordScreenshots());
        capabilities.setCapability("idle-timeout", getIdleTimeoutSeconds());
        capabilities.setCapability("command-timeout", getCommandTimeoutSeconds());
        capabilities.setCapability("max-duration", getMaxDurationSeconds());
        capabilities.setCapability("deviceOrientation",getDeviceOrientation());
        if (resolution != null && !resolution.equals("")) {
            capabilities.setCapability("screen-resolution", resolution);
        }
        return capabilities;
    }
}
