package pages;

import org.openqa.selenium.By;

import static data.Constants.BASE_URL;

public class HomePage extends BasePage<HomePage> {

    final protected By buyMoisturizersButton = By.cssSelector("a[href*='moisturizer']");
    final protected By buySunscreenButton = By.cssSelector("a[href*='sunscreen']");
    final protected By currentTemperatureSpan = By.id("temperature");

    @Override
    protected String getPageUrl() {
        return BASE_URL;
    }

    public String getCurrentTemperature() {
        return getText(currentTemperatureSpan);
    }

    public MoisturizerPage clickBuyMoisturizersButton() {
        click(buyMoisturizersButton);
        return new MoisturizerPage();
    }


}
