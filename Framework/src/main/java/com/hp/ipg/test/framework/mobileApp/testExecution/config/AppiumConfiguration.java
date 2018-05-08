package com.hp.ipg.test.framework.mobileApp.testExecution.config;

import com.hp.ipg.test.framework.mobileApp.testExecution.clients.AppiumGridClient;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.util.logging.Level;

@Configuration
public class AppiumConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumConfiguration.class);

    private static final Device DEFAULT_DEVICE = Device.iPhone8;
    private static final Platform DEFAULT_PLATFORM = Platform.IOS;
    private static final String DEVICE_PROPERTY = "appium.deviceName";
    private static final String VERSION_PROPERTY = "appium.platformVersion";
    private static final String PLATFORM_PROPERTY = "appium.platformName";

    private static Device device;
    private static Platform platform;
    private static String version;

    @PostConstruct
    private void init() throws MalformedURLException {
        device = AppiumConfiguration.getDevice();
        platform = AppiumConfiguration.getPlatform();
        version = AppiumConfiguration.getVersion();

        System.setProperty(DEVICE_PROPERTY, device.getName());
        System.setProperty(PLATFORM_PROPERTY, platform.getName());
        System.setProperty(VERSION_PROPERTY, version);
        LOGGER.info(String.format("\nAppium configuration:\n\tdevice:\t%s\n\tversion:\t%s\n\tplatform:\t%s",
                device.getName(),
                version,
                platform.getName()));
        if (platform == Platform.IOS) {
            //can be used, if anything specfic to platform comes up
        }
    }

    public static Device getDevice() {
        if (device == null) {
            String browserProp = System.getProperty(DEVICE_PROPERTY);
            if (browserProp == null || browserProp.isEmpty()) {
                browserProp = DEFAULT_DEVICE.toString();
            }
            device = Device.fromString(browserProp);
        }
        return device;
    }

    public static Platform getPlatform() {
        if (platform == null) {
            String platformProp = System.getProperty(PLATFORM_PROPERTY);
            if (platformProp == null || platformProp.isEmpty()) {
                platformProp = DEFAULT_PLATFORM.toString();
            }
            platform = Platform.fromString(platformProp);
        }
        return platform;
    }

    public static String getVersion() {
        if (version == null) {
            String versionProp = System.getProperty(VERSION_PROPERTY);
            if (versionProp == null) {
                versionProp = "";
            }
            version = versionProp;
        }
        return version;
    }

    public static IOSDriver getRemoteDriver(@NotNull AppiumGridClient appiumGridClient, String bundleId) {
        return new IOSDriver(appiumGridClient.getHubUrl(), appiumGridClient.getiOSCapabilities(bundleId));
    }

    public static AndroidDriver getRemoteDriver(@NotNull AppiumGridClient appiumGridClient, String appPackage, String appActivity) {
        return new AndroidDriver(appiumGridClient.getHubUrl(), appiumGridClient.getAndroidCapabilities(appPackage, appActivity));
    }
}
