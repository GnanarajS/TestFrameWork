package com.hp.ipg.test.framework.mobileApp.pageObjectBase;

import com.hp.ipg.test.framework.mobileApp.utils.DefaultValues;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppiumPageBase extends PageBase {
private static final Logger LOGGER = LoggerFactory.getLogger(AppiumPageBase.class);

    public AppiumPageBase(AppiumDriver driver) {
        super(driver);
    }

    public WebElement waitForVisibilityOfElementLocated(final By locator) {
        return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisibilityOfElementLocated(final WebElement element) {
        return getWebDriverWait().until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForInvisibilityOfElementLocated(final By locator) {
        getWebDriverWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForInvisibilityOfElementLocated(final WebElement webElement) {
        getWebDriverWait().until(ExpectedConditions.invisibilityOf(webElement));
    }

    public WebElement waitForElementToBeClickable(final By locator) {
        return getWebDriverWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementToBeClickable(final WebElement element) {
        return getWebDriverWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForPresenceOfElementLocated(final By locator) {
        return getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void waitForTextToBePresentInElementLocated(final By locator, final String text) {
        getWebDriverWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public void waitForTextToBePresentInElementValue(final WebElement element, final String text) {
        getWebDriverWait().until(ExpectedConditions.textToBePresentInElementValue(element, text));
    }

    public void waitForInvisibilityOfElementWithText(final By locator, final String text) {
        getWebDriverWait().until(ExpectedConditions.invisibilityOfElementWithText(locator, text));
    }

    public void waitForAttributeToBe(final By locator, final String attribute, final String value) {
        getWebDriverWait().until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    public void waitForAttributeToBe(final WebElement element, final String attribute, final String value) {
        getWebDriverWait().until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public void waitForAttributeContains(final By locator, final String attribute, final String value) {
        getWebDriverWait().until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    public void waitForAttributeContains(WebElement element, final String attribute, final String value) {
        getWebDriverWait().until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    public void waitForVisibilityOfNotification(WebElement element){
        getWebDriverWait(driver, DefaultValues.DEFAULT_NOTIFICATION_TIMEOUT_SEC, DefaultValues.DEFAULT_POLL_TIMEOUT_MS).until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForTitle(final String title) {
        getWebDriverWait().until(ExpectedConditions.titleIs(title));
    }

    public WebDriverWait getWebDriverWait() {
        return new WebDriverWait(driver, DefaultValues.DEFAULT_NOTIFICATION_TIMEOUT_SEC, DefaultValues.DEFAULT_INITIALIZE_ELEMENT_TIMEOUT_SEC);
    }

    public WebDriverWait getWebDriverWait(WebDriver driver, long timeOutInSeconds, long sleepInMillis) {
        return new WebDriverWait(driver, timeOutInSeconds, sleepInMillis);
    }

    public void waitForConditionToBeTrue(WebDriver driver, ExpectedCondition<Boolean> flag) {
        getWebDriverWait().until(flag);
    }

    public void waitAndClick(By locator) {
        waitForElementToBeClickable(locator);
        LOGGER.info("Performing click action ... ");
        click(locator);
    }

    public void waitAndClick(WebElement webElement) {
        waitForElementToBeClickable(webElement);
        LOGGER.info("Performing click action ... ");
        webElement.click();
    }

    public void waitAndType(By locator, String keyword) {
        waitForElementToBeClickable(locator);
        type(locator, keyword);
        driver.hideKeyboard();
    }

    public void waitAndType(MobileElement webElement, String keyword) {
        waitForElementToBeClickable(webElement);
        type(webElement, keyword);
        driver.hideKeyboard();
    }

    @Override
    public String getText(MobileElement webElement) {
        waitForVisibilityOfElementLocated(webElement);
        return super.getText(webElement);
    }
}