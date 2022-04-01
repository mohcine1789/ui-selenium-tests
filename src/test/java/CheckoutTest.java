import lombok.SneakyThrows;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.ConfirmationPage;
import pages.HomePage;

import static org.hamcrest.MatcherAssert.assertThat;

public class CheckoutTest extends BaseTest {

    @Parameters(value = {"browser"})
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(String browser) {
        getDriver(browser);
    }

    @SneakyThrows
    @Test
    public void userCanPurchaseMoisturizerSuccessfully() {
        new HomePage().open()
                .clickBuyMoisturizersButton()
                .clickAddButton()
                .clickCartButton()
                .clickGoToCheckoutButton()
                .fillOutForm()
                .clickBuyButton();

        assertThat("Payment confirmation failed or message is incorrect.",
                new ConfirmationPage().isConfirmationMessageDisplayed());
    }

}