package com.hp.ipg.test.framework.webApp.pageObjectBase;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SeleniumPageBase extends PageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumPageBase.class);

    public SeleniumPageBase(WebDriver webDriver) {
        super(webDriver);
    }

    public static final int DEFAULT_NAVIGATION_TIMEOUT_SEC = 120;
    private static final int DEFAULT_POLL_TIMEOUT_MS = 200;
    private static final int VISIBILITY_TIMEOUT_SECONDS = 5;
    private static final int POLL_DELAY_MILLISECONDS = 100;

    public WebDriverWait getWebDriverWait() {
        return new WebDriverWait(webDriver, DEFAULT_NAVIGATION_TIMEOUT_SEC, DEFAULT_POLL_TIMEOUT_MS);
    }

    //Use it like : wait(ExpectedConditions.presenceOfElementLocated(locator)), wait(ExpectedConditions.textToBePresentInElementLocated(locator, text))
    public void wait(ExpectedCondition ec) {
        getWebDriverWait().until(ec);
    }

    //Use it like : waitForConditionToBeTrue(PageUtils.absenseOfElementLocatedBy(locator))
    public void waitForConditionToBeTrue(ExpectedCondition<Boolean> flag) {
        getWebDriverWait().until(flag);
    }

    public void waitForPageReady() {
        getWebDriverWait().until(pageReady());
    }

    public WebDriverWait getWebDriverWait(WebDriver driver, long timeOutInSeconds, long sleepInMillis) {
        return new WebDriverWait(driver, timeOutInSeconds, sleepInMillis);
    }

    public void waitForInputFocus(final WebElement element) {
        getWebDriverWait().until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver from) {
                return element.equals(from.switchTo().activeElement());
            }
        });
    }

    /*
     * Executes the given javascript on the browser.
     * @throws WebDriverException if any errors occur.
     * @returns the result from the javascript executed.
     */
    public Object executeJavascript(WebDriver webDriver, String javascript) {
        LOGGER.debug("executeJavaScript executing:" + javascript);

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        Object retVal = js.executeScript(javascript);

        LOGGER.debug("executeJavaScript returning:" + retVal);
        return retVal;
    }

    @Override
    protected void waitForPage() {
        waitForPageReady();
    }

    protected WebElement findElement(By locator) {
        return webDriver.findElement(locator);
    }

    protected void waitAndClick(By locator, ExpectedCondition ec) {
        wait(ec);
        findElement(locator).click();
    }

    /**
     * Used in WebDriverWait to wait for an existing element to no longer exist.
     */
    public ExpectedCondition<Boolean> absenseOfElementLocatedBy(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                boolean elementIsGone = false;
                try {
                    driver.findElement(locator);
                } catch (NoSuchElementException e) {
                    elementIsGone = true;
                }
                return elementIsGone;
            }
        };
    }

    public ExpectedCondition<Boolean> absenseOfAllElementsLocatedBy(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List<WebElement> elements = driver.findElements(locator);
                return elements.size() == 0 ? true : false;
            }
        };
    }

    public ExpectedCondition<Boolean> titleEquals(final String title) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.getTitle().equals(title);
            }
        };
    }

    public ExpectedCondition<Boolean> elementContainsText(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElement(locator).getText().length() > 0;
            }
        };
    }

    public ExpectedCondition<Boolean> urlContains(final String urlSubstring) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.getCurrentUrl().contains(urlSubstring);
            }
        };
    }

    public ExpectedCondition<Boolean> pageReady() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                boolean result = false;
                String readyState = "";
                try {
                    readyState = (String) executeJavascript(driver, "return document.readyState;");
                } catch (WebDriverException e) {
                    LOGGER.debug("Caught " + e);
                }

                if ("complete".equals(readyState)) {
                    result = true;
                }
                return result;
            }
        };
    }

    /**
     * Used in WebDriverWait to wait for an existing element to be active
     */
    public ExpectedCondition<Boolean> elementToBeActive(final WebElement webElement) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.switchTo().activeElement() != null
                        && driver.switchTo().activeElement().equals(webElement);
            }
        };
    }
}
