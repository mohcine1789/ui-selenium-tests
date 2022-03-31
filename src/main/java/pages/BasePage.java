package pages;

import driver.DriverManagerFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

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
        return isElementVisible(by, 2);
    }

    public boolean isElementPresent(By locator) {
        return isElementPresent(locator, 3);
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

    @Step("Check if element is clickable")
    protected boolean isElementClickable(By by) {
        try {
            findClickableElement(by);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected int getNumberOfElements(By by) {
        return getNumberOfElements(by, 2);
    }

    @Step("Get number of elements: {by}")
    protected int getNumberOfElements(By by, int timeOutInSeconds) {
        return isElementVisible(by, timeOutInSeconds) ? driver.findElements(by).size() : 0;
    }

    @Step("Get text from element: {by}")
    protected String getText(By by) {
        return findElement(by).getText();
    }

    @Step("Get text from element")
    protected String getText(WebElement element) {
        return findElement(element).getText();
    }

    @Step("Get value of attribute: {attribute} for: {by}")
    protected String getValueAttributeFor(By by, String attribute) {
        return findPresentElement(by).getAttribute(attribute);
    }

    @Step("Wait for element invisibility: {by}")
    protected void waitForElementInvisibility(By by) {
        wait(by, ExpectedConditions.invisibilityOfElementLocated(by));
    }

    @Step("Wait for attribute: {attribute} to be not empty: {by} ")
    protected boolean waitForNotEmptyAttribute(By by, String attribute) {
        return wait(by, ExpectedConditions.attributeToBeNotEmpty(findElement(by), attribute));
    }

    @Step("Wait for attribute: {attribute} contains: {value} at element: {by}")
    protected boolean waitForAttributeContains(By by, String attribute, String value) {
        return wait(by, ExpectedConditions.attributeContains(findElement(by), attribute, value));
    }

    @Step("Wait for attribute: {attribute} not contains: {value} at element: {by}")
    protected boolean waitForAttributeNotContain(By by, String attribute, String value) {
        return wait(by, ExpectedConditions.not(ExpectedConditions.attributeContains(findElement(by), attribute, value)));
    }

    @Step("Wait for number of windows be: {number}")
    protected boolean waitForNumberOfWindowsToBe(int number) {
        return wait.until(ExpectedConditions.numberOfWindowsToBe(number));
    }

    @Step("Wait for number of elements: {by} to be more than: {number}")
    protected void waitForNumberOfElementToBeMoreThan(By by, int number) {
        wait(by, ExpectedConditions.numberOfElementsToBeMoreThan(by, number));
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

    @Step("Find element: {element}")
    protected WebElement findElement(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    @Step("Find elements: {by}")
    protected List<WebElement> findElements(By by) {
        return wait(by, ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    @Step("Find clickable element: {by}")
    protected WebElement findClickableElement(By by) {
        return wait(by, ExpectedConditions.elementToBeClickable(by));
    }

    @Step("Find clickable element: {element}")
    protected WebElement findClickableElement(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Find present element: {by}")
    protected WebElement findPresentElement(By by) {
        return wait(by, ExpectedConditions.presenceOfElementLocated(by));
    }

    @Step("Find present element: {by}")
    private WebElement findPresentElement(By by, int timeOutInSeconds) {
        return new WebDriverWait(driver, timeOutInSeconds, 30)
                .withMessage(String.format("Timed out waiting for: %s", by.toString()))
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }

    @Step("Find present elements: {by}")
    protected List<WebElement> findPresentElements(By by) {
        return wait(by, ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    @Step("Click: {by}")
    protected void click(By by) {
        findClickableElement(by).click();
    }

    @Step("Click: {element}")
    protected void click(WebElement element) {
        findClickableElement(element).click();
    }

    @Step("Click: {by} by index: {index}")
    protected void clickByIndex(By by, int index) {
        findElements(by).get(index).click();
    }

    @Step("Move to: {by} by index: {index}")
    protected void moveToElementByIndex(By by, int index) {
        moveToElement(findElements(by).get(index));
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

    @Step("Move to element {element}")
    protected void moveToElement(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    @Step("Move to element: {by}")
    protected void moveToElement(By by) {
        moveToElement(findElement(by));
    }

    @Step("Move and click element: {element}")
    protected void moveAndClick(WebElement element) {
        new Actions(driver).moveToElement(element).click().perform();
    }

    @Step("Select: {element} with value: {value}")
    protected void selectByValue(WebElement element, String value) {
        new Select(findClickableElement(element)).selectByValue(value);
    }

    @Step("Select: {by} with value: {value}")
    protected void selectByVisibleText(By by, String value) {
        new Select(findClickableElement(by)).selectByVisibleText(value);
    }

    @Step("Select: {by} by index: {value}")
    protected void selectByIndex(By by, int index) {
        new Select(findElement(by)).selectByIndex(index);
    }

    @Step("Get first option: {by}")
    protected String getFirstSelectedOption(By by) {
        return new Select(findElement(by)).getFirstSelectedOption().getText();
    }

    @Step("Switch to window with index {index}")
    protected WebDriver switchToWindow(int index) {
        return driver.switchTo().window(getWindowHandles().get(index).toString());
    }

    @Step("Get window handles")
    private List getWindowHandles() {
        return new ArrayList(driver.getWindowHandles());
    }

    @Step("Switch to iFrame {by}")
    protected void switchToiFrame(By by) {
        driver.switchTo().frame(findElement(by));
    }

    @Step("Accept alert")
    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    @Step("Get current url")
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Load {url}")
    protected void load(String url) {
        driver.get(url);
    }

    @Step("Refresh")
    protected void refresh() {
        driver.navigate().refresh();
    }

    @Step("Add cookie {cookie.name}")
    protected void addCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
    }

    @Step("Wait for url to be {url}")
    protected boolean waitForUrlToBe(String url) {
        return wait.until(ExpectedConditions.urlToBe(url));
    }

    @Step("Type character by character: {by} text {text}")
    public void typeCharacterByCharacter(By by, String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String s = new StringBuilder().append(c).toString();
            findElement(by).sendKeys(s);
        }
    }

    @Step("Get Alert text")
    protected String getAlertText() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }
}