package pages;

import driver.DriverManagerFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public abstract class BasePage<T> {
    protected WebDriver driver;
    protected WebDriverWait wait;

    protected abstract String getPageUrl();

    @SuppressWarnings("unchecked")
    public T open() {
        load(getPageUrl());
        return (T) this;
    }


    protected BasePage() {
        this.driver = DriverManagerFactory.getDriverManager().getDriver();
        this.wait = new WebDriverWait(driver, 30, 30);
    }

    @Step("Check if element is visible: {by}")
    protected boolean isElementVisible(By by, int timeOutInSeconds) {
        try {
            findElement(by, timeOutInSeconds);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    protected boolean isElementVisible(By by) {
        return isElementVisible(by, 10);
    }

    @Step("Check if element is present: {locator}")
    public boolean isElementPresent(By locator, int timeOutInSeconds) {
        try {
            findPresentElement(locator, timeOutInSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("Get number of elements: {by}")
    protected int getNumberOfElements(By by, int timeOutInSeconds) {
        return isElementVisible(by, timeOutInSeconds) ? driver.findElements(by).size() : 0;
    }

    @Step("Get text from element: {by}")
    protected String getText(By by) {
        return findElement(by).getText();
    }


    @Step("Wait for attribute: {attribute} contains: {value} at element: {by}")
    protected boolean waitForAttributeContains(By by, String attribute, String value) {
        return wait(by, ExpectedConditions.attributeContains(findElement(by), attribute, value));
    }

    private <T> T wait(By by, ExpectedCondition<T> expectedCondition) {
        return wait.withMessage(String.format("Timed out waiting for: %s", by.toString())).until(expectedCondition);
    }

    @Step("Find element: {by}")
    protected WebElement findElement(By by) {
        return wait(by, ExpectedConditions.visibilityOfElementLocated(by));
    }

    @Step("Find element: {by}")
    protected WebElement findElement(By by, int timeOutInSeconds) {
        return new WebDriverWait(driver, timeOutInSeconds, 50)
                .withMessage(String.format("Timed out waiting for: %s", by.toString()))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    @Step("Find clickable element: {by}")
    protected WebElement findClickableElement(By by) {
        return wait(by, ExpectedConditions.elementToBeClickable(by));
    }

    @Step("Find present element: {by}")
    private WebElement findPresentElement(By by, int timeOutInSeconds) {
        return new WebDriverWait(driver, timeOutInSeconds, 30)
                .withMessage(String.format("Timed out waiting for: %s", by.toString()))
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }
    @Step("Click: {by}")
    protected void click(By by) {
        findClickableElement(by).click();
    }

    @Step("Clear: {by} and fill with: {text}")
    protected void clearAndFill(By by, String text) {
        clearAndFill(findElement(by), text);
    }

    @Step("Clear: {element} and fill with: {text}")
    protected void clearAndFill(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    @Step("Send keys: {by} to: {var1}")
    protected void sendKeys(By by, CharSequence var1) {
        findElement(by).sendKeys(var1);
    }

    @Step("Switch to iFrame {by}")
    protected void switchToiFrame(By by) {
        driver.switchTo().frame(findElement(by));
    }

    @Step("Switch to default content")
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    @Step("Load {url}")
    protected void load(String url) {
        driver.get(url);
    }

    @Step("Type character by character: {by} text {text}")
    public void typeCharacterByCharacter(By by, String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String s = new StringBuilder().append(c).toString();
            findElement(by).sendKeys(s);
        }
    }
}