package steps;


import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.DashboardActivity;
import groupf.taes.ipleiria.spots.R;
import modelo.SpotsManager;


public class US1FeatureWithConnectionSteps extends GreenCoffeeSteps {

    @Given("^I am an anonymous user $")
    public void i_am_an_anonymous_user() {
        Assert.assertNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I open the application $")
    public void i_open_the_application() {
        onViewWithText(R.string.app_name);
    }

    @Then("^I see the parking lot map of the Campus (\\d+) - Park A$")
    public void i_see_the_parking_lot_map_of_the_Campus_Park_A(int arg1) {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithText(string(R.string.anonParkTitle)).isDisplayed();
    }

    @Then("^I see the number of free and occupied sports $")
    public void i_see_the_number_of_free_and_occupied_sports() {
        onViewWithId(R.id.txtNumberFreeSpots).isDisplayed();
        onViewWithId(R.id.txtNumberFreeSpots).contains(String.valueOf(SpotsManager.INSTANCE.getFreeSpotsParkA()));

        onViewWithId(R.id.txtNumberOcuppiedSpots).isDisplayed();
        onViewWithId(R.id.txtNumberOcuppiedSpots).contains(String.valueOf(SpotsManager.INSTANCE.getOcuppiedSpotsParkA()));
    }

    @Then("^I see the markers of free spots displayed on the map$")
    public void i_see_the_markers_of_free_spots_displayed_on_the_map() {
        Assert.assertEquals(DashboardActivity.getMarkers().size(), SpotsManager.INSTANCE.getFreeSpotsParkA());
    }

    @Then("^I see the last update date$")
    public void i_see_the_last_update_date() {
        onViewWithId(R.id.lastInfoDate).isNotEmpty();
        onViewWithId(R.id.lastInfoDate).contains(SpotsManager.INSTANCE.getDateOfData());
    }

    @Then("^I see the button to Login$")
    public void i_see_the_button_to_Login() {
        onViewWithId(R.id.btnSignup).isDisplayed();
        onViewWithId(R.id.btnSignup).contains(string(R.string.signUpButton));
    }
}
