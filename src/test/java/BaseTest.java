
import driver.Browser;
import driver.DriverManager;
import driver.DriverManagerFactory;
import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

@Slf4j
@Listeners(ScreenshotListener.class)
public abstract class BaseTest {

    protected WebDriver driver;
    private DriverManager driverManager;
    private boolean shouldQuitDriverAfterTestMethod = true;

    @SuppressWarnings("UnusedReturnValue")
    protected WebDriver getDriver(String browserName) {
        driverManager = DriverManagerFactory.getManager(Browser.getBrowserByName(browserName));
        log.info("Connecting with browser = {}...", browserName);
        driver = driverManager.getDriver("http://localhost:4444/wd/hub");

        return driver;
    }


    protected void quitDriver() {
        driverManager.quitDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void quitDriverAfterTestMethod() {
        if (shouldQuitDriverAfterTestMethod) {
            quitDriver();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void onFailureActions(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            this.logFailureDetails(testResult);
        }
    }

    private void logFailureDetails(ITestResult testResult) {
        String methodName = testResult.getMethod().getMethodName();

        log.error("----------------------------------------------------------------------------------------" +
                        "\n | FAILED: | {} \n | Console logs: | {} ",
                methodName, getShortenedSeleniumFailureMessage(testResult) +
                        "\n---------------------------------------------------------------------------------");
    }

    @Attachment("Failure Message")
    private String getShortenedSeleniumFailureMessage(ITestResult testResult) {
        return removeBuildInfo(
                removeExpectedConditionFailed(
                        removeTimedOutFailed(testResult.getThrowable().getMessage())));
    }

    private String removeExpectedConditionFailed(String input) {
        return substringStartingAtRegex(input, "Expected condition failed: ");
    }

    private String removeTimedOutFailed(String input) {
        return substringStartingAtRegex(input, "Timed out after [0-9]+ seconds: ");
    }

    private String substringStartingAtRegex(String input, String regex) {
        String[] splitResult = input.split(regex);

        if (substringWasFound(splitResult)) {
            return splitResult[1];
        } else {
            return input;
        }
    }

    private String removeBuildInfo(String input) {
        int suffixIndex = input.indexOf("Build info:");
        return suffixIndex != -1 ? input.substring(0, suffixIndex) : input;
    }

    private boolean substringWasFound(String[] splittedString) {
        return splittedString.length == 2;
    }
}