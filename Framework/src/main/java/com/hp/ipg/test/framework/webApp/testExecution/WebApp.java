package com.hp.ipg.test.framework.webApp.testExecution;

import com.hp.ipg.test.framework.webApp.testExecution.clients.WebSauceClient;

public class WebApp extends RoamUI {

    protected WebApp() {}

    protected WebApp(WebSauceClient sauceClient) {
        super(sauceClient);
    }
}
