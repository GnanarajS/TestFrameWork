package com.hp.ipg.test.framework.mobileApp.testExecution;

import com.hp.ipg.test.framework.mobileApp.testExecution.clients.AppiumGridClient;

public class MobileApp extends Roam{

    protected static final String PLATFORM_PROPERTY = "appium.platformName";

    protected MobileApp(AppiumGridClient appiumGridClient) {
        super(appiumGridClient);
    }
}
