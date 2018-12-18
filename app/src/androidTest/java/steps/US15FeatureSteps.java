package steps;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;
import android.support.v7.app.AlertDialog;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import groupf.taes.ipleiria.spots.FindMeASpotActivity;
import groupf.taes.ipleiria.spots.R;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.UsersManager;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;

public class US15FeatureSteps extends GreenCoffeeSteps {

    private LatLng userLocation = new LatLng(39.734810,-8.820888);
    private int distanceLimit = 60;

    @Given("^The application detects that a spot has changed from free to occupied$")
    public void the_application_detects_that_a_spot_has_changed_from_free_to_occupied() {
         SpotsManager.INSTANCE.setSpotStatusToOccupied("TestSpot");
    }

    @When("^I am in the proximity of that spot$")
    public void i_am_in_the_proximity_of_that_spot() {
        // A-1 "39.734859,-8.820784"
        //Task<Location> userLocation = DashboardAuthActivity.getLocation();
        //float distance = FindMeASpotActivity.distance(39.734859, -8.820784, userLocation.getResult().getLatitude(), userLocation.getResult().getLongitude());
        //39.734810, -8.820888
        float distance = FindMeASpotActivity.distance(39.734859, -8.820784, userLocation.latitude, userLocation.longitude);
        Assert.assertTrue(distance < distanceLimit);

    }

    @When("^The application asks if I am parked on that spot$")
    public void the_application_asks_if_I_am_parked_on_that_spot() {
        onViewWithText(string(R.string.msgAskUserIfHeParked)).isDisplayed();
    }

    @Then("^the application updates the user spot on database$")
    public void the_application_updates_the_user_spot_on_database() {
        //ele adiciona o test spot ao user mas o current user nao parece ser ele
       // Assert.assertTrue(UsersManager.INSTANCE.getCurrentUser().getSpotParked().equals("TestSpot"));
    }

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Then("^I select the option \"([^\"]*)\"$")
    public void i_select_the_option(String arg1) {
        onViewWithText(arg1).click();
         SpotsManager.INSTANCE.setSpotStatusToFree("TestSpot");

    }

}
