package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.UsersManager;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US16FeatureIsParkedSteps extends GreenCoffeeSteps {


    @Given("^I am authenticated user$")
    public void i_am_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Given("^I am already parked$")
    public void i_am_already_parked() {
        Assert.assertNotNull(UsersManager.INSTANCE.getCurrentUser().getSpotParked());
    }

    @When("^I press the \"([^\"]*)\" on the menu$")
    public void i_press_the_on_the_menu(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I see a message saying that I am already parked$")
    public void i_see_a_message_saying_that_I_am_already_parked() {
        onViewWithText(string(R.string.errorUserAlreadyParked)).isDisplayed();
        onViewWithText(string(R.string.OK)).click();
    }

    @Then("^I am on the dashboard auth$")
    public void i_am_on_the_dashboard_auth() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
    }

}
