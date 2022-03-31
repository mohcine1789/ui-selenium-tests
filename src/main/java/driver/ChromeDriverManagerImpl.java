package driver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static java.util.Objects.isNull;


public class ChromeDriverManagerImpl extends DriverManager {

    @Override
    public void startService() {
        if (isNull(driverService.get())) {
            WebDriverManager manager = ChromeDriverManager.getInstance(DriverManagerType.CHROME);
            manager.setup();
            driverService.set(new ChromeDriverService.Builder().usingDriverExecutable(new File(manager.getBinaryPath()))
                    .usingAnyFreePort().build());
            try {
                driverService.get().start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--start-maximized");
        options.setHeadless(BooleanUtils.toBoolean(System.getProperty("headless", "false")));
        driverMap.put(getCurrentThreadId(), new ChromeDriver((ChromeDriverService) driverService.get(), options));
        driverMap.get(getCurrentThreadId()).manage().window().maximize();
        maximizeScreenResolution();
        return driverMap.get(getCurrentThreadId());
    }

    @Override
    public WebDriver createDriver(String url) {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, PageLoadStrategy.NORMAL);
        capabilities.setCapability("headless", System.getProperty("headless", "false"));
        capabilities.setCapability("enableVNC", BooleanUtils.toBoolean(System.getProperty("vnc", "false")));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        try {
            driverMap.put(getCurrentThreadId(), new RemoteWebDriver(new URL(url), capabilities));
            maximizeScreenResolution();
        } catch (MalformedURLException ignored) {
        }

        return driverMap.get(getCurrentThreadId());
    }
}