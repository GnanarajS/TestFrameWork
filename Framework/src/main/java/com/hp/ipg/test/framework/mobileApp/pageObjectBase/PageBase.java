package com.hp.ipg.test.framework.mobileApp.pageObjectBase;

import com.hp.ipg.test.framework.mobileApp.testObjects.Directions;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.hp.ipg.test.framework.mobileApp.utils.DefaultValues;

public abstract class PageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageBase.class);
    protected AppiumDriver driver;

    public PageBase(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, DefaultValues.DEFAULT_INITIALIZE_ELEMENT_TIMEOUT_SEC, TimeUnit.SECONDS), this);
    }

    protected TouchAction getTouchInstance() {
        return new TouchAction(driver);
    }

    public void click(By locator) {
        LOGGER.info("Performing click action ... ");
        driver.findElement(locator).click();
    }

    public void click(MobileElement mobileElement) {
        LOGGER.info("Performing click action ... ");
        mobileElement.click();
    }

    public void type(By locator, String keyword) {
        LOGGER.info(String.format("Typing %s ...",keyword));
        driver.findElement(locator).sendKeys(keyword);
    }

    public void type(MobileElement mobileElement, String keyword) {
        LOGGER.info(String.format("Typing %s ...",keyword));
        mobileElement.sendKeys(keyword);
    }

    public String getText(By locator) {
        return driver.findElement(locator).getText();
    }

    public String getText(MobileElement mobileElement) {
        return mobileElement.getText();
    }

    public void touch(MobileElement mobileElement){
        getTouchInstance().tap(mobileElement).perform();
    }

    public void press(MobileElement mobileElement) {
        getTouchInstance().press(mobileElement).perform();
    }

    public void tap(MobileElement mobileElement) {
        getTouchInstance().tap(mobileElement).perform();
    }

    public void longPress(MobileElement mobileElement) {
        getTouchInstance().longPress(mobileElement).perform();
    }

    public void longPress(MobileElement mobileElement, Duration duration) {
        getTouchInstance().longPress(mobileElement, duration).perform();
    }

    public Dimension getScreenSize() {
        return driver.manage().window().getSize();

    }

    /**
     * Swipes right on the given MobileElement
     * @param mobileElement
     */
    public void swipeRight(MobileElement mobileElement) {
        LOGGER.info("Swiping Right ...");
        Dimension size = getWindowSize();
        int end_x = (int) (size.width * DefaultValues.DEFAULT_HORIZONTAL_START_POINT_MULTIPLIER);
        int start_x = (int) (size.width * DefaultValues.DEFAULT_HORIZONTAL_END_POINT_MULTIPLIER);
        getTouchInstance().press(mobileElement, start_x, 0).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_LEFT_SCROLL_TIMEOUT_SEC)).moveTo(end_x, 0).release().perform();
    }

    /**
     * Swipes left on the given MobileElement
     * @param mobileElement
     */
    public void swipeLeft(MobileElement mobileElement) {
        LOGGER.info("Swiping Left ...");
        Dimension size = getWindowSize();
        int x1 = (int) (size.width * DefaultValues.DEFAULT_HORIZONTAL_END_POINT_MULTIPLIER);
        getTouchInstance().press(mobileElement).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_LEFT_SCROLL_TIMEOUT_SEC)).moveTo(x1,0).release().perform();
    }
    
    public void swipeRightToLeft(MobileElement mobileElement) {
    	LOGGER.info("Swiping Right to Left ...");
    	Dimension size = getWindowSize();
    	int start_x = (int) (size.width * DefaultValues.DEFAULT_HORIZONTAL_START_POINT_MULTIPLIER);
    	int end_x = (int) (size.width * DefaultValues.DEFAULT_HORIZONTAL_END_POINT_MULTIPLIER);
    	getTouchInstance().press(mobileElement, start_x, 0).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_LEFT_SCROLL_TIMEOUT_SEC)).moveTo(end_x, 0).release().perform();
    }

    public void partialSwipeLeft(MobileElement mobileElement) {
        LOGGER.info("Swiping Left ...");
        Dimension size = getWindowSize();
        int x1 = (int) (size.width * DefaultValues.DEFAULT_HORIZONTAL_END_POINT_MULTIPLIER);
        getTouchInstance().press(mobileElement).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC)).moveTo(x1,0).release().perform();
    }

    public void partialSwipeRight(MobileElement mobileElement) {
        LOGGER.info("Swiping Right ...");
        int x = mobileElement.getCenter().getX();
        getTouchInstance().press(mobileElement).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC)).moveTo(-(x/2),0).release().perform();
    }

    public void swipeVertical() {
        LOGGER.info("Swiping Vertically ...");
        getTouchInstance().press((int)(getScreenSize().getHeight() *0.3), (int)(getScreenSize().getWidth() * 0.5)).
                waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC)).press((int)(getScreenSize().getHeight() *0.7), (int)(getScreenSize().getWidth() * 0.5)).
                release().perform();
    }

    public void swipeVertical(MobileElement element1, MobileElement element2, Directions directions ) {
        LOGGER.info("Swiping " + directions + " vertically between given two elements ...");
        Point point1 = element1.getCenter();
        int x1 = point1.getX();
        int y1 = point1.getY();

        Point point2 = element2.getCenter();
        int x2 = point2.getX();
        int y2 = point2.getY();

        TouchAction touchAction = new TouchAction(driver);

        switch(directions) {
            case UP:
                touchAction.press(x2, y2).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC)).moveTo(x1, y1).release().perform();
                break;
            case DOWN:
                touchAction.press(x1, y1).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC)).moveTo(x2, y2).release().perform();
                break;
            default:
                throw new IllegalArgumentException("The given Direction:" + directions + " is not valid for this operation. ");
        }
    }


    public void swipeHorizontal(Directions directions) {
        LOGGER.info("Swiping " + directions + " Horizontally ...");
        TouchAction touchAction = new TouchAction(driver);
        Dimension size = getWindowSize();
        int anchor = (int) (size.height * DefaultValues.DEFAULT_HORIZONTAL_ANCHOR_MULTIPLIER);
        int startPoint = (int) (size.width * DefaultValues.DEFAULT_HORIZONTAL_START_POINT_MULTIPLIER);
        int endPoint = (int) (size.width * DefaultValues.DEFAULT_HORIZONTAL_END_POINT_MULTIPLIER);
        switch(directions) {
            case LEFT:
                touchAction.press(startPoint, anchor).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC)).moveTo(endPoint, anchor).release().perform();
                break;
            case RIGHT:
                touchAction.press(endPoint, anchor).waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC)).moveTo(startPoint, anchor).release().perform();
                break;
            default:
                throw new IllegalArgumentException("The given Direction:" + directions + " is not valid for this operation. ");
        }
    }

    public void cancel() {
        LOGGER.info("Cancelling ...");
        getTouchInstance().cancel();
    }

    public boolean isElementExist(By locator) {
        if (!driver.findElements(locator).isEmpty()) {
            LOGGER.info("Element does exists");
            return true;
        } else {
            LOGGER.info("Element does not exists");
            return false;
        }
    }

    public void swipeUp(MobileElement element){
        Dimension size = getWindowSize();
        int endy = (int) (size.height * DefaultValues.DEFAULT_VERTICAL_ANCHOR_MULTIPLIER);
        int endx = (int)(size.width * DefaultValues.DEFAULT_HORIZONTAL_ANCHOR_MULTIPLIER);
        getTouchInstance().press(element)
                .waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC))
                .moveTo(endx, endy)
                .release().perform();
    }

    public void swipeDown(MobileElement element){
        Dimension size = getWindowSize();
        int endy = (int)(size.height * DefaultValues.DEFAULT_HORIZONTAL_ANCHOR_MULTIPLIER);
        getTouchInstance().press(element)
                .waitAction(Duration.ofSeconds(DefaultValues.DEFAULT_SCROLL_TIMEOUT_SEC))
                .moveTo(DefaultValues.ZERO, endy)
                .release().perform();
    }

    public void typeAndEnter(MobileElement mobileElement, String keyword) {
        LOGGER.info(String.format("Typing %s ...",keyword));
        mobileElement.sendKeys(keyword, Keys.ENTER);
    }

    public Dimension getWindowSize() {
        LOGGER.info("Getting window size ...");
        return driver.manage().window().getSize();
    }

    public String getPageSource(){
        return driver.getPageSource();
    }
}
