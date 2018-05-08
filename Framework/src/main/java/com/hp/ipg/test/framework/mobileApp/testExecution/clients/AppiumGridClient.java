package com.hp.ipg.test.framework.mobileApp.testExecution.clients;

import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.logging.Level;

@Component
public abstract class AppiumGridClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumGridClient.class);
    private static final String ROAM_APP_ID = "com.hp.roam";
    private static final String TEST_OBJECT_ACCESS_KEY = "3BF45027726547DB904F2AC1F361A0F2";
    protected GridType gridType;

    @Value("${appium.platformName:iOS}")
    private String platformName;
    @Value("${appium.platformVersion:11.1}")
    private String platformVersion;
    @Value("${appium.deviceName:iPhone 6}")
    private String deviceName;
    @Value("${appium.appPath:}")
    private String appPath;
    @Value("${device.udid:}")
    private String udid;
    @Value("${appium.appID:}")
    private String appID;

    protected AppiumGridClient() {
        setGridType();
    }

    public abstract URL getHubUrl();

    protected abstract void setGridType();

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getUdid() {
        return udid;
    }

    public String getAppPath() {
        return appPath;
    }

    public String getAppID() {
        return appID;
    }

    protected abstract boolean useSauce();

    public DesiredCapabilities getiOSCapabilities(String bundleId) {
        LOGGER.info("Setting up capabilities ...");
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", getPlatformName());
        capabilities.setCapability("platformVersion", getPlatformVersion());
        capabilities.setCapability("deviceName", getDeviceName());
        if(useSauce()) {
            capabilities.setCapability("testobjectApiKey", TEST_OBJECT_ACCESS_KEY);
        }
        else {
            capabilities.setCapability("browserName", "");
            capabilities.setCapability("udid", getUdid());
        }

        if(bundleId.equals(ROAM_APP_ID)) {
            if(useSauce()) {
                capabilities.setCapability("testobject_app_id", getAppID());
            }
            else {
                capabilities.setCapability("app", getAppPath());
            }
        }
        else {
            capabilities.setCapability("bundleId", bundleId);
        }
        LoggingPreferences log = new LoggingPreferences();
        log.enable(LogType.DRIVER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, log);

        return capabilities;
    }

    public DesiredCapabilities getAndroidCapabilities(String appPackage, String appActivity) {
        LOGGER.info("Setting up capabilities ...");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", getPlatformName());
        capabilities.setCapability("platformVersion", getPlatformVersion());
        capabilities.setCapability("deviceName", getDeviceName());
        if(useSauce()) {
            capabilities.setCapability("testobjectApiKey", TEST_OBJECT_ACCESS_KEY);
        }
        capabilities.setCapability("appPackage",appPackage);
        capabilities.setCapability("appActivity",appActivity);
        if(appPackage.equals(ROAM_APP_ID)) {
            if(useSauce()) {
                capabilities.setCapability("testobject_app_id", getAppID());
            }
            else {
                capabilities.setCapability("app", getAppPath());
            }
        }

        LoggingPreferences log = new LoggingPreferences();
        log.enable(LogType.DRIVER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, log);

        return capabilities;
    }

    public enum GridType {
        LOCAL("Local"),
        SAUCELABS("Saucelabs");

        private String gridType;

        GridType(String gridTypeString) {
            gridType = gridTypeString;
        }

        public static GridType fromString(String gridTypeString) {
            for (GridType resolution : GridType.values()) {
                if (resolution.toString().equalsIgnoreCase(gridTypeString)) {
                    return resolution;
                }
            }

            throw new IllegalArgumentException("The grid type " + gridTypeString + " is not defined.");
        }

        @Override
        public String toString() {
            return gridType;
        }
    }
}
