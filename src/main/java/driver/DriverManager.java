package driver;

import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.currentThread;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public abstract class DriverManager {

    protected static Map<Long, WebDriver> driverMap = new ConcurrentHashMap<>();
    protected static ThreadLocal<DriverService> driverService = new ThreadLocal<>();

    protected abstract void startService();

    private void stopService() {
        if (nonNull(driverService.get()) && driverService.get().isRunning())
            driverService.get().stop();
    }

    protected abstract WebDriver createDriver();

    protected abstract WebDriver createDriver(String url);

    public void quitDriver() {
        if (nonNull(driverMap.get(currentThread().getId()))) {
            driverMap.get(getCurrentThreadId()).quit();
            stopService();
            driverMap.remove(getCurrentThreadId());
        }

    }

    public WebDriver getDriver() {
        if (isNull(driverMap.get(getCurrentThreadId()))) {
            startService();
            createDriver();
        }
        return driverMap.get(getCurrentThreadId());

    }

    public WebDriver getDriver(String url) {
        if (isNull(driverMap.get(getCurrentThreadId()))) {
            createDriver(url);
        }

        return driverMap.get(getCurrentThreadId());
    }

    protected long getCurrentThreadId() {
        return currentThread().getId();
    }

    protected void maximizeScreenResolution() {
        if (nonNull(driverMap.get(getCurrentThreadId()))) {
            int width = NumberUtils.createInteger(((RemoteWebDriver) driverMap.get(getCurrentThreadId()))
                    .executeScript("return window.screen.availWidth").toString());
            int height = NumberUtils.createInteger(((RemoteWebDriver) driverMap.get(getCurrentThreadId()))
                    .executeScript("return window.screen.availHeight").toString());
            driverMap.get(getCurrentThreadId()).manage().window().setSize(new Dimension(width, height));
        }
    }
}