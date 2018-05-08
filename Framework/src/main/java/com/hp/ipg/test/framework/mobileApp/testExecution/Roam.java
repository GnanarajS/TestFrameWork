package com.hp.ipg.test.framework.mobileApp.testExecution;

import com.hp.ipg.test.framework.mobileApp.testExecution.clients.AppiumGridClient;
import com.hp.ipg.test.framework.mobileApp.testExecution.config.AppiumConfiguration;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

public abstract class Roam {
    private static final Logger LOGGER = LoggerFactory.getLogger(Roam.class);
    public static final String TEST_OBJECT_TEST_REPORT_URL = "testobject_test_report_url";

    private AppiumDriver driver;
    private final AppiumGridClient appiumGridClient;

    protected Roam(AppiumGridClient appiumGridClient) {
        this.appiumGridClient = appiumGridClient;
    }

    public AppiumDriver getDriver() {
        return driver;
    }

    public AppiumDriver getDriver(String bundleId) {
        if (driver == null) {
          driver = AppiumConfiguration.getRemoteDriver(appiumGridClient, bundleId);
        }
        return driver;
    }

    public AppiumDriver getDriver(String appPackage, String appActivity) {
        if (driver == null) {
            driver = AppiumConfiguration.getRemoteDriver(appiumGridClient, appPackage, appActivity);
        }
        return driver;
    }

    public void setWebDriver(AppiumDriver driver) {
        this.driver = driver;
    }

    protected boolean saveBrowserLog(File file) {
        boolean success = false;
        if (driver != null) {
            try {
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file, true);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                if (file.exists()) {
                    osw.write("============================== Logs ============================\n");
                    for (String logType : driver.manage().logs().getAvailableLogTypes()) {
                        osw.write("\n\n:: LOG TYPE: " + logType + "\n");
                        osw.write("===============================================================\n");
                        LogEntries logEntries = driver.manage().logs().get(logType);
                        for (LogEntry entry : logEntries) {
                            osw.write(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage() + "\n");
                        }
                    }
                    osw.close();
                    success = true;
                } else {
                    LOGGER.warn("Unable to save the appium log.");
                }
            } catch (Exception e) {
                LOGGER.warn("Unable to save the appium log. Error: " + e.getMessage());
            }
        }
        return success;
    }

    protected void takeBrowserScreenshot(File file) { }

    public void cleanup() {
        if (driver != null) {
            LOGGER.info("Quiting the Driver...");
            driver.quit();
        }
    }
}
