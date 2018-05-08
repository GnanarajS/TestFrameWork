package com.hp.ipg.test.framework.mobileApp.pageObjectBase;

import java.util.List;

import org.openqa.selenium.By;

import com.hp.ipg.test.framework.mobileApp.testObjects.Directions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class AndroidPageBase extends AppiumPageBase {
    public AndroidPageBase(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(id = "com.android.systemui:id/dismiss_task")
    private MobileElement dismissHpRoamApp;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='HP Roam']")
    private MobileElement openHpRoamApp;

    @AndroidFindBy(id = "android:id/search_src_text")
    private MobileElement searchText;

    @AndroidFindBy(id = "com.android.systemui:id/dismiss_text")
    private MobileElement clearAllNotifications;

    @AndroidFindBy(id = "com.sec.android.app.launcher:id/home_allAppsIcon")
    private MobileElement homeAllAppsButton;

    @AndroidFindBy(accessibility = "HP Roam")
    private MobileElement hpRoamInSearchList;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Print']")
    protected MobileElement printOptionButton;

    @AndroidFindBy(id = "com.android.packageinstaller:id/permission_allow_button")
    private MobileElement permissionAllowButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Your document has been added to your queue']")
    private MobileElement fileAddedQueueNotification;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Documents ready to print']")
    private MobileElement documentReadyToPrintNotification;

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Choose']")
    private MobileElement chooseButtonInNotification;

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Print']")
    private MobileElement printButtonInNotification;

    @AndroidFindBy(id = "android:id/inbox_text0")
    private MobileElement printerNameInNotification;

    @AndroidFindBy(id = "android:id/inbox_text1")
    private MobileElement jobCountInNotification;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Successfully Released Job']")
    private MobileElement successfullyReleasedJobNotification;

    private static final By clearAllNotificationBtn = By.id("com.android.systemui:id/dismiss_text");
    private static final By notificationTitle = By.id("android:id/title");
    private static final By TEST_HOOK_NOTIFICATION = By.id("android:id/right_icon");
    private static final By EMAIL_ERROR_MESSAGE = By.id("com.hp.roam:id/email_error_tv");
    private static final By FAKE_PRINTER_ONE = By.xpath("//android.widget.Button[@content-desc='Fake Printer 1']");
    private static final By FAKE_PRINTER_TWO = By.xpath("//android.widget.Button[@content-desc='Fake Printer 2']");
    private static final By PRINT_BUTTON_IN_NOTIFICATION = By.xpath("//android.widget.Button[@content-desc='Print']");
    private static final By CHOOSE_BUTTON_IN_NOTIFICATION = By.xpath("//android.widget.Button[@content-desc='Choose']");
    private static final By ACCEPT_AND_CONTINUE_BUTTON = By.id("com.android.chrome:id/terms_accept");
    private static final By JOB_COUNT_IN_NOTIFICATION = By.id("android:id/inbox_text1");
    private static final By DOCUMENT_READY_TO_PRINT = By.xpath("//android.widget.TextView[@text='Documents ready to print']");

    @AndroidFindBy(id = "email_error_tv")
    private MobileElement errorMessage;

    @AndroidFindBy(id = "ca_error_tv")
    private MobileElement invalidEmailerrorMessage;

    @AndroidFindBy(id = "login_error_tv")
    private MobileElement loginErrorMessage;

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc='Navigate up']")
    private MobileElement backButton;

    @AndroidFindBy(id = "android:id/right_icon")
    private MobileElement testHookNotification;

    @AndroidFindBy(id = "com.android.chrome:id/url_bar")
    private MobileElement showMeHowURL;

    @AndroidFindBy(id = "com.android.chrome:id/terms_accept")
    private MobileElement acceptAndContinueButton;

    @AndroidFindBy(id = "com.android.chrome:id/negative_button")
    private MobileElement noThanksButton;

    public AndroidPageBase closeApp() {
        driver.closeApp();
        return this;
    }

    public AndroidPageBase closeHpRoamInRecentApps(){
        // To open the recent apps
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.KEYCODE_APP_SWITCH);
        waitAndClick(dismissHpRoamApp);
        // To click the Home button
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.HOME);
        return this;
    }

    public AndroidPageBase clickHpRoamInRecentApps(){
        // To open the recent apps
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.KEYCODE_APP_SWITCH);
        waitAndClick(openHpRoamApp);
        return this;
    }

    public AndroidPageBase goBackOnePage(){
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        return this;
    }

    public Boolean isFileAddedQueueNotificationVisible() {
        waitForVisibilityOfElementLocated(fileAddedQueueNotification);
        return true;
    }

    public String getEmailErrorMessage() {
        waitForVisibilityOfElementLocated(errorMessage);
        return errorMessage.getText();
    }

    public String getInvalidEmailErrorMessage() {
        waitForVisibilityOfElementLocated(invalidEmailerrorMessage);
        return invalidEmailerrorMessage.getText();
    }

    public String getLoginErrorMessage() {
        waitForVisibilityOfElementLocated(loginErrorMessage);
        return loginErrorMessage.getText();
    }

    public boolean isErrorMessageDisplayed() {
        return isElementExist(EMAIL_ERROR_MESSAGE);
    }

    public boolean isInvalidEmailErrorMessageDisplayed() {
        return invalidEmailerrorMessage.isDisplayed();
    }

    public void openNotification() {
        ((AndroidDriver) driver).openNotifications();
    }
    
    public void clickFakeBeacon(By mobileElement) {
    	int count = 0;
        while (!isElementExist(TEST_HOOK_NOTIFICATION) && count < 5) {
            ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
            openNotification();
            count++;
        }
        if(isElementExist(mobileElement)) {
        	waitAndClick(mobileElement);
        }
        else { 
        	swipeDown(testHookNotification);
        	waitAndClick(mobileElement);
        }
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
    }

    // To make fake printer available
    public void triggerFakeBeaconOne() {
        openNotification();
        clickFakeBeacon(FAKE_PRINTER_ONE);
    }
    
    public void triggerFakeBeaconTwo() {
        openNotification();
        clickFakeBeacon(FAKE_PRINTER_TWO);
    }

    public void expandHPRoamNotification() {
        if(!isElementExist(PRINT_BUTTON_IN_NOTIFICATION)) {
        	swipeDown(documentReadyToPrintNotification);
        }
    }

    public void clearAllNotifications() {
        openNotification();
        List<MobileElement> notifications = driver.findElements(notificationTitle);
        while (driver.findElements(clearAllNotificationBtn).isEmpty()){
            swipeVertical(notifications.get(1), notifications.get(notifications.size()-1), Directions.UP);
            notifications = driver.findElements(notificationTitle);
        }
        waitAndClick(clearAllNotifications);
    }

    public AndroidPageBase clickPermissionAllowButton() {
        waitAndClick(permissionAllowButton);
        return this;
    }

    public AndroidPageBase clickFileAddedQueueNotification() {
        fileAddedQueueNotification.click();
        return this;
    }

    public AndroidPageBase clickHomeButton() {
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.HOME);
        return this;
    }

    public AndroidPageBase clickBackButton() {
        waitAndClick(backButton);
        return this;
    }

    public String getBrowserBarURL() {
        if (isElementExist(ACCEPT_AND_CONTINUE_BUTTON))
        {
            waitAndClick(acceptAndContinueButton);
            waitAndClick(noThanksButton);
        }
        return waitForVisibilityOfElementLocated(showMeHowURL).getText();
    }

    public void waitForDocumentReadyNotification(){
        waitForVisibilityOfNotification(documentReadyToPrintNotification);
    }

    public boolean doesPrinterNameNotificationMessageExists() {
        return printerNameInNotification.isDisplayed();
    }

    public boolean doesJobCountNotificationMessageExists() {
        return jobCountInNotification.isDisplayed();
    }

    public String getPageCountFromNotification() {
        if(isElementExist(JOB_COUNT_IN_NOTIFICATION)){
            return jobCountInNotification.getText();
        } else {
            swipeDown(documentReadyToPrintNotification);
            return jobCountInNotification.getText();
        }
    }

    public boolean doesSuccessfullyReleasedJobNotificationExists() {
        return successfullyReleasedJobNotification.isDisplayed();
    }

    public boolean isDocumentReadyNotificationMessageExists() {
        return isElementExist(DOCUMENT_READY_TO_PRINT);
    }

    public void clickPrintButtonInNotification(){
        waitAndClick(printButtonInNotification);
    }

    public void clickChooseButtonInNotification(){
        waitAndClick(chooseButtonInNotification);
    }

    @Override
    public void waitAndType(MobileElement webElement, String keyword) {
        waitForElementToBeClickable(webElement);
        type(webElement, keyword);
    }
}
