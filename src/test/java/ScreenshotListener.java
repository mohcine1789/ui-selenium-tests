import driver.DriverManagerFactory;
import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Objects.nonNull;

@Slf4j
public class ScreenshotListener extends TestListenerAdapter {

    private static final String DS = System.getProperty("file.separator");
    private static final String SCREENSHOT_DIRECTORY = "target/screenshots";
    private static final String SCREENSHOT_EXTENSION = ".png";

    @Override
    public void onTestFailure(ITestResult testResult) {
        byte[] screenshot = getScreenShot();
        if (nonNull(screenshot)) {
            saveToFile(screenshot, getFileName(testResult.getName()));
        } else {
            log.error("Something went wrong, screenshot was not taken");
        }
    }

    @Attachment(value = "Screen shot", type = "image/png", fileExtension = ".png")
    private byte[] getScreenShot() {
        try {
            WebDriver driver = DriverManagerFactory.getDriverManager().getDriver();
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (WebDriverException e) {
            log.error("Selenium screenshot capture failed: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void saveToFile(byte[] bytes, String fileName) {
        try {
            FileUtils.writeByteArrayToFile(new File(fileName), bytes);
        } catch (IOException e) {
            log.error("There was an issue creating the screenshot {}: {}", fileName, e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getFileName(String methodName) {
        return SCREENSHOT_DIRECTORY + DS + methodName + "_" + new SimpleDateFormat("dd-MM-yyyy_HH-mm-sss").format(new Date()) + SCREENSHOT_EXTENSION;
    }
}