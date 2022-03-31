import lombok.SneakyThrows;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.ConfirmationPage;
import pages.HomePage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CheckoutTest extends BaseTest {

    private static final String PAYMENT_SUCCESS_MESSAGE = "PAYMENT SUCCESS";

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

        assertThat("Payment failed or payment success message is incorrect.",
                new ConfirmationPage().getConfirmationMessage(),
                equalTo(PAYMENT_SUCCESS_MESSAGE));
    }

}