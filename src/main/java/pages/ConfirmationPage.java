package pages;

import org.openqa.selenium.By;

public class ConfirmationPage extends BasePage<ConfirmationPage> {

    final protected By confirmationMessage = By.xpath("//*[contains(text(),'SUCCESS') or contains(text(),'FAILED')]");

    @Override
    protected String getPageUrl() {
        return null;
    }

    public Boolean isConfirmationMessageDisplayed() {
        switchToDefaultContent();
        return isElementVisible(confirmationMessage);
    }
}
