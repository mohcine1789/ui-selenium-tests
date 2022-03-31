package pages;

import org.openqa.selenium.By;

import static data.Constants.*;

public class CartPage extends BasePage<CartPage> {

    final protected By goToCheckoutButton = By.cssSelector("button[type='submit']");
    final protected By emailInput = By.cssSelector(".bodyView #email");
    final protected By cardNumberInput = By.id("card_number");
    final protected By ccExpInput = By.id("cc-exp");
    final protected By ccCvcInput = By.id("cc-csc");
    final protected By payButton = By.id("submitButton");
    final protected By billingZip = By.id("billing-zip");
    final protected By frame = By.className("stripe_checkout_app");

    @Override
    protected String getPageUrl() {
        return null;
    }

    public CartPage clickGoToCheckoutButton() {
        click(goToCheckoutButton);
        return this;
    }

    public void clickBuyButton() {
        click(payButton);
    }

    public CartPage fillOutForm() {
        switchToiFrame(frame);
        clearAndFill(emailInput, USER_EMAIL);
        waitForAttributeContains(emailInput, "value", USER_EMAIL);
        typeCharacterByCharacter(cardNumberInput, CARD_NUMBER);
        typeCharacterByCharacter(ccExpInput, CARD_EXPIRY_DATE);
        clearAndFill(ccCvcInput, CARD_CVC_NUMBER);
        clearAndFill(billingZip, ZIP_CODE);
        return this;
    }
}
