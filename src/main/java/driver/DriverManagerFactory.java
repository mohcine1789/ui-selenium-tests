package driver;


import static java.util.Objects.isNull;

public class DriverManagerFactory {
    private static ThreadLocal<DriverManager> driverManager = new ThreadLocal<>();


    public static DriverManager getManager(Browser type) {
        switch (type) {
            case CHROME:
                driverManager.set(new ChromeDriverManagerImpl());
                break;
            default:
                driverManager.set(new ChromeDriverManagerImpl());
                break;
        }
        return driverManager.get();

    }

    /**
     * Gets driverMap manager.
     *
     * @return the driverMap manager
     */
    public static DriverManager getDriverManager() {
        if (isNull(driverManager.get())) {
            driverManager.set(getManager(Browser.valueOf(System.getProperty("browser", "chrome").toUpperCase())));
        }
        return driverManager.get();
    }
}