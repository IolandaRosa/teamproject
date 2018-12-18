package steps;

import android.location.Location;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import groupf.taes.ipleiria.spots.FindMeASpotActivity;
import groupf.taes.ipleiria.spots.R;
import modelo.UsersManager;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.support.test.espresso.action.ViewActions.click;

public class US15FeatureSteps extends GreenCoffeeSteps {

    @Given("^the application detects that a spot has changed from free to occupied$")
    public void the_application_detects_that_a_spot_has_changed_from_free_to_occupied() {

    }

    @When("^I am in the proximity of that spot$")
    public void i_am_in_the_proximity_of_that_spot() {
        // A-1 "39.734859,-8.820784"
        Task<Location> userLocation = DashboardAuthActivity.getLocation();
        float distance = FindMeASpotActivity.distance(39.734859, -8.820784, userLocation.getResult().getLatitude(), userLocation.getResult().getLongitude());


    }

    @When("^The application asks if I am parked on that spot$")
    public void the_application_asks_if_I_am_parked_on_that_spot() {
        onViewWithText(string(R.string.msgAskUserIfWantToPark)).isDisplayed();
    }

    @When("^I select the option yes$")
    public void i_select_the_option_yes() {
        onViewWithText(string(R.string.Yes)).click();
    }

    @Then("^the application updates the user spot on database$")
    public void the_application_updates_the_user_spot_on_database() {
        Assert.assertNotNull(UsersManager.INSTANCE.getCurrentUser().getSpotParked());
    }

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Then("^I select the option \"([^\"]*)\"$")
    public void i_select_the_option(String arg1) {
        onViewWithText(string(R.string.msgAskUserIfWantToPark)).isDisplayed();
        onViewWithText(arg1).click();
    }
}
