package steps;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.R;
import modelo.SpotsManager;

public class US1FeatureWithoutConnectionSteps extends GreenCoffeeSteps {

    @Given("^I am an anonymous user$")
    public void i_am_an_anonymous_user() {
        Assert.assertNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I open the application $")
    public void i_open_the_application() {
        onViewWithText(R.string.app_name);
    }

    @When("^I don't have internet connection $")
    public void i_don_t_have_internet_connection() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithText(string(R.string.anonParkTitle)).isDisplayed();
    }

    @Then("^I see the date that the information was last updated$")
    public void i_see_the_date_that_the_information_was_last_updated() {
        onViewWithId(R.id.lastInfoDate).isNotEmpty();
        onViewWithId(R.id.lastInfoDate).contains(SpotsManager.getINSTANCE().getDateOfData());
    }

}
