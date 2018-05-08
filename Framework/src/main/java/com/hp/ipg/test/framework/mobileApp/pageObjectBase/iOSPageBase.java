package com.hp.ipg.test.framework.mobileApp.pageObjectBase;

import java.time.Duration;
import java.util.UUID;

import com.hp.ipg.test.framework.mobileApp.testObjects.Directions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class iOSPageBase extends AppiumPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(iOSPageBase.class);

    public String email = "Roam-User" + UUID.randomUUID() + "@mail.hpfoghorn.com";
    public static final String PASSWORD = "User1234";

    @iOSFindBy(accessibility = "Print")
    protected MobileElement airPrint;

    @iOSFindBy(xpath = "//XCUIElementTypeStaticText[@name='HP Roam']")
    protected MobileElement roamPrinter;

    @iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name='Printer Options']/XCUIElementTypeButton[2]")
    protected MobileElement printButton;

    @iOSFindBy(accessibility = "Printer")
    protected MobileElement selectPrinter;

    @iOSFindBy(accessibility = "HP Roam")
    protected MobileElement localAirPrinter;

    @iOSFindBy(xpath = "//XCUIElementTypeTextField[@value='user name']")
    protected MobileElement userNameTextBox;

    @iOSFindBy(xpath = "//XCUIElementTypeSecureTextField[@value='Password']")
    protected MobileElement passwordTextBox;

    @iOSFindBy(accessibility = "OK")
    protected MobileElement ok;

    @iOSFindBy(xpath = "//XCUIElementTypeTextField[@value='Email']")
    protected MobileElement emailTextBox;

    @iOSFindBy(id = "com.hp.roam:id/ca_error_tv")
    private MobileElement errorMessage;

    @iOSFindBy(accessibility = "Back")
    private MobileElement backButton;

    @iOSFindBy(accessibility = "Skip")
    private MobileElement skipButton;

    @iOSFindBy(accessibility = "Cancel")
    protected MobileElement cancelButton;

    private static final By GET_STARTED_BUTTON = By.name("Get started");

    public iOSPageBase(AppiumDriver driver) {
        super(driver);
    }

    protected void print(String email, String password) {
        waitAndClick(airPrint);
        waitAndClick(selectPrinter);
        waitAndClick(localAirPrinter);
        waitAndClick(printButton);
        waitForElementToBeClickable(userNameTextBox);
        type(userNameTextBox,email);
        type(passwordTextBox, password);
        touch(ok);
    }

    public String getErrorMessage(String locator) {
        return waitForVisibilityOfElementLocated(By.id(locator)).getText();
    }

    public void clearTextBox(By locator) {
        int len = driver.findElement(locator).getText().length();
        for(int i =0; i< len; i++) {
            ((IOSDriver) driver).getKeyboard().sendKeys(Keys.BACK_SPACE);
        }
    }

    public iOSPageBase enterEmail(String email) {
        typeAndEnter(emailTextBox, email);
        return this;
    }

    public iOSPageBase enterPassword(String password){
        typeAndEnter(passwordTextBox, password);
        return this;
    }

    public iOSPageBase closeApp() {
        driver.closeApp();
        return this;
    }

    public void runAppInBackgruound() {
        (driver).runAppInBackground(Duration.ofSeconds(1));
    }

    public void runAppInBackgruound(int durationInSeconds) {
        (driver).runAppInBackground(Duration.ofSeconds(durationInSeconds));
    }

    public iOSPageBase replaceEmail(String previousEmail, String newEmail) {
        waitAndClick(By.xpath("//XCUIElementTypeTextField[@value='"+previousEmail+"']"));
        clearTextBox(By.xpath("//XCUIElementTypeTextField[@value='"+previousEmail+"']"));
        enterEmail(newEmail);
        return this;
    }

    public boolean isErrorMessageDisplayed(String message) {
        return driver.getPageSource().contains(message);
    }

    public iOSPageBase clickBackButton(){
        backButton.click();
        return this;
    }

    public iOSPageBase clickSkipButton(){
        waitAndClick(skipButton);
        return this;
    }

    public MobileElement getSkipButton(){
        return skipButton;
    }

    @Override
    public void waitAndClick(WebElement webElement) {
        waitForVisibilityOfElementLocated(webElement);
        LOGGER.info("Performing click action ... ");
        webElement.click();
    }

    public iOSPageBase clickOkButton(){
        ok.click();
        return this;
    }

    public iOSPageBase clickGetStartedButton(){
        int count = 0;
        do {
            swipeHorizontal(Directions.RIGHT);
            count ++;
        } while (!isElementExist(GET_STARTED_BUTTON) && count < 6);
        waitAndClick(GET_STARTED_BUTTON);
        return this;
    }
}
