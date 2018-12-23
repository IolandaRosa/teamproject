package steps;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.FindMeASpotActivity;
import groupf.taes.ipleiria.spots.R;
import modelo.SpotsManager;

public class US15WithNoOptionFeatureSteps extends GreenCoffeeSteps {

    private LatLng userLocation = new LatLng(39.734810,-8.820888);
    private int distanceLimit = 60;
    @Given("^The application detects that a spot has changed from free to occupied$")
    public void the_application_detects_that_a_spot_has_changed_from_free_to_occupied() {
         SpotsManager.INSTANCE.setSpotStatusToOccupied("TestSpot");
    }

    @When("^I am in the proximity of that spot$")
    public void i_am_in_the_proximity_of_that_spot() {
        float distance = FindMeASpotActivity.distance(39.734859, -8.820784, userLocation.latitude, userLocation.longitude);
        Assert.assertTrue(distance < distanceLimit);

    }

    @When("^The application asks if I am parked on that spot$")
    public void the_application_asks_if_I_am_parked_on_that_spot() {
        onViewWithText(string(R.string.msgAskUserIfHeParked)).isDisplayed();
    }

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Then("^I select the option \"([^\"]*)\"$")
    public void i_select_the_option(String arg1) {
        onViewWithText(arg1).click();
    }


}
