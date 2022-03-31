package pages;

import org.openqa.selenium.By;

public class ConfirmationPage extends BasePage<ConfirmationPage> {

    final protected By confirmationMessage = By.xpath("//*[text()='PAYMENT SUCCESS']");

    @Override
    protected String getPageUrl() {
        return null;
    }

    public String getConfirmationMessage() {
        return getText(confirmationMessage);
    }

}
