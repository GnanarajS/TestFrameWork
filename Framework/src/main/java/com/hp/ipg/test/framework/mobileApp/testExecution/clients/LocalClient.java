package com.hp.ipg.test.framework.mobileApp.testExecution.clients;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class LocalClient extends AppiumGridClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalClient.class);

    private static final String SIMULATOR_HUB_URL = "http://127.0.0.1:4723/wd/hub";

    @Override
    public URL getHubUrl() {
        try {
            LOGGER.info("Using [{}] hub [{}]...", this.getClass().getName(), SIMULATOR_HUB_URL);
            return new URL(SIMULATOR_HUB_URL);
        } catch (MalformedURLException e) {
            String exceptionMessage = String.format("Hub URL is invalid: [%s]. inner exception: [%s: %s]",
                    SIMULATOR_HUB_URL, e.getClass().getName(), e.getMessage());
            throw new RuntimeException(exceptionMessage);
        }
    }

    @Override
    protected boolean useSauce() {
        return false;
    }

    @Override
    public DesiredCapabilities getiOSCapabilities(String bundleId) {
        DesiredCapabilities capabilities = super.getiOSCapabilities(bundleId);
        return capabilities;
    }

    @Override
    public DesiredCapabilities getAndroidCapabilities(String appPackage, String appActivity) {
        DesiredCapabilities capabilities = super.getAndroidCapabilities(appPackage, appActivity);
        return capabilities;
    }

    @Override
    protected void setGridType() {
        super.gridType = GridType.LOCAL;
    }
}
