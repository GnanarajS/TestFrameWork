package com.hp.ipg.test.framework.webApp.testExecution.config;

import com.hp.ipg.test.framework.webApp.testExecution.clients.WebSauceClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

@Configuration
public class SeleniumConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumConfiguration.class);

    public static final Browser DEFAULT_BROWSER = Browser.CHROME;
    private static final Platform DEFAULT_PLATFORM = Platform.MAC_OSX10_9;

    private static final String BROWSER_PROPERTY = "selenium.browserName";
    private static final String VERSION_PROPERTY = "selenium.version";
    private static final String PLATFORM_PROPERTY = "selenium.platformName";

    private static final String CHROME_FILE_PATH_MAC_OSX = "src/main/resources/chromedriver";
    private static final String CHROME_FILE_PATH_WINDOWS = "src/main/resources/chromedriver.exe";

    private static final String CHROME_DRIVER_KEY = "webdriver.chrome.driver";

    private static Browser browser;
    private static Platform platform;
    private static String version;

    @PostConstruct
    private void init() throws MalformedURLException {
        browser = SeleniumConfiguration.getBrowser();
        platform = SeleniumConfiguration.getPlatform();
        version = SeleniumConfiguration.getVersion();

        System.setProperty(BROWSER_PROPERTY, browser.getName());
        System.setProperty(PLATFORM_PROPERTY, platform.getName());
        System.setProperty(VERSION_PROPERTY, version);

        LOGGER.info(String.format("\nSelenium configuration:\n\tbrowser:\t%s\n\tversion:\t%s\n\tplatform:\t%s",
                browser.getName(),
                version,
                platform.getName()));
    }

    public static Browser getBrowser() {
        if (browser == null) {
            String browserProp = System.getProperty(BROWSER_PROPERTY);
            if (browserProp == null || browserProp.isEmpty()) {
                browserProp = DEFAULT_BROWSER.toString();
            }
            browser = Browser.fromString(browserProp);
        }
        return browser;
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

    public static WebDriver getLocalDriver() {

        WebDriver driver = null;
        if (getBrowser() == Browser.FIREFOX) {
            driver = new FirefoxDriver();
            ((FirefoxDriver) driver).setLogLevel(Level.ALL);
        }
        else if (getBrowser() == Browser.CHROME) {
            try {
                switch(getPlatform().toString()){
                    case "windows":
                        File wFile = new File(SeleniumConfiguration.CHROME_FILE_PATH_WINDOWS);
                        System.setProperty(CHROME_DRIVER_KEY, wFile.getCanonicalPath().toString());
                        break;
                    case "OS X 10.9":
                        File mFile = new File(SeleniumConfiguration.CHROME_FILE_PATH_MAC_OSX);
                        System.setProperty(CHROME_DRIVER_KEY, mFile.getPath().toString());
                        break;
                    default:
                        throw new IllegalArgumentException(getPlatform().toString() + " is not a valid name for a Platform.");
                }

            } catch (IOException e) {
                throw new RuntimeException("Error setting up path to chromeDriver.", e);
            }

             driver = new ChromeDriver();
            ((ChromeDriver) driver).setLogLevel(Level.ALL);
        }
        else {
            throw new UnsupportedOperationException(String.format("This method does not support the '%s' browser.", getBrowser().getName()));
        }

        driver.get("about:blank");

        return driver;
    }

    public static WebDriver getRemoteDriver(@NotNull WebSauceClient webSauceClient) {
        WebDriver webDriver = new RemoteWebDriver(webSauceClient.getHubUrl(), webSauceClient.getCapabilities());
        ((RemoteWebDriver) webDriver).setLogLevel(Level.ALL);

        // This allows us to upload files even when running against the selenium grid.
        ((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
        return webDriver;
    }
}
