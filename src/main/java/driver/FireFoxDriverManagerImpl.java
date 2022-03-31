package driver;

import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.currentThread;
import static java.util.Objects.isNull;

public class FireFoxDriverManagerImpl extends DriverManager {

    @Override
    public void startService() {
        if (isNull(driverService.get())) {
            WebDriverManager manager = WebDriverManager.getInstance(DriverManagerType.FIREFOX);
            manager.setup();
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            driverService.set(new GeckoDriverService.Builder().usingDriverExecutable(new File(manager.getBinaryPath()))
                    .usingAnyFreePort().build());
            try {
                driverService.get().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected WebDriver createDriver() {
        FirefoxOptions options = new FirefoxOptions().setLogLevel(FirefoxDriverLogLevel.FATAL)
                .setHeadless(BooleanUtils.toBoolean(System.getProperty("headless", "false")));
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        driverMap.put(getCurrentThreadId(), new FirefoxDriver(((GeckoDriverService) driverService.get()), options));
        driverMap.get(getCurrentThreadId()).manage().window().maximize();
        return driverMap.get(currentThread().getId());
    }

    @Override
    public WebDriver createDriver(String url) {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        capabilities.setCapability("headless", System.getProperty("headless", "false"));
        capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, PageLoadStrategy.NORMAL);
        capabilities.setCapability("enableVNC", BooleanUtils.toBoolean(System.getProperty("vnc", "false")));
        try {
            driverMap.put(currentThread().getId(), new RemoteWebDriver(new URL(url), capabilities));
            maximizeScreenResolution();
        } catch (MalformedURLException ignored) {
        }
        return driverMap.get(getCurrentThreadId());
    }
}