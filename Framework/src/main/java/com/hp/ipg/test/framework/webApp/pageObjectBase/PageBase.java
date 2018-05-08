package com.hp.ipg.test.framework.webApp.pageObjectBase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class PageBase {
    protected WebDriver webDriver;

    public PageBase(WebDriver driver) {
        this.webDriver = driver;
        waitForPage();
        PageFactory.initElements(driver, this);
    }

    protected abstract void waitForPage();
}
