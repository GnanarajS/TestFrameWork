package com.hp.ipg.test.framework.webApp.testExecution;

import com.hp.ipg.test.framework.webApp.testExecution.clients.WebSauceClient;
import com.hp.ipg.test.framework.webApp.testExecution.config.SeleniumConfiguration;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RoamUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoamUI.class);

    private WebDriver driver;
    private WebSauceClient sauceClient;

    protected RoamUI() {}

    protected RoamUI(WebSauceClient sauceClient) {
        this.sauceClient = sauceClient;
    }

    public WebDriver getDriver() {
        if (driver == null) {
            if(Boolean.parseBoolean(System.getProperty("sauce.enable"))) {
                driver = SeleniumConfiguration.getRemoteDriver(sauceClient);
            }
            else {
                driver = SeleniumConfiguration.getLocalDriver();
            }
        }
        return driver;
    }


    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
