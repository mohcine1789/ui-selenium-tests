package pages;

import org.openqa.selenium.By;

public class MoisturizerPage extends BasePage<MoisturizerPage> {

    final protected By addButton = By.className("btn-primary");
    final protected By cartButton = By.id("cart");

    @Override
    protected String getPageUrl() {
        return null;
    }

    public MoisturizerPage clickAddButton() {
        click(addButton);
        return this;
    }

    public CartPage clickCartButton() {
        click(cartButton);
        return new CartPage();
    }

}
