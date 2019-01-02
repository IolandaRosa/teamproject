package steps;

import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.v7.app.AlertDialog;
import android.test.TouchUtils;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.UsersManager;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US19FeatureSteps extends GreenCoffeeSteps {


    @Given("^I am an authenticated user and I am leaving my spot$")
    public void i_am_an_authenticated_user_and_I_am_leaving_my_spot() {
     Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
     SpotsManager.INSTANCE.setSpotStatusToFree("TestSpot");
    }

    @Given("^I see a message saing I want to leave \"([^\"]*)\"$")
    public void i_see_a_message_saing_I_want_to_leave(String arg1) {
        onView(withText(string(R.string.askUserIsLeavingTheSpot))).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Given("^I select the option \"([^\"]*)\"$")
    public void i_select_the_option(String arg1) {
       onViewWithText(arg1).click();

    }

    @Given("^I see a screen to give a rate to that spot$")
    public void i_see_a_screen_to_give_a_rate_to_that_spot() {
        onViewWithId(R.id.ratingBar).isDisplayed();
        onViewWithId(R.id.btnSendRate).isDisplayed();
        onViewWithId(R.id.btnAddToFavourites).isDisplayed();
        onViewWithId(R.id.btnClose).isDisplayed();
    }

    @Given("^I insert the rate value$")
    public void i_insert_the_rate_value() {
        onViewWithId(R.id.ratingBar).click();
    }

    @Given("^The button to send rate$")
    public void the_button_to_send_rate() {
        onViewWithId(R.id.btnSendRate).click();
    }


    @Then("^The spot is displayed free on the dashboard$")
    public void the_spot_is_displayed_free_on_the_dashboard() {
        Spot spot = SpotsManager.INSTANCE.getSpotFromId("TestSpot");
        Assert.assertEquals(0, spot.getStatus());
    }


    @Then("^I am on the auth dashboard$")
    public void i_am_on_the_auth_dashboard() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
        onViewWithId(R.id.drawer_layout).isDisplayed();
    }

    @Then("^I am parked$")
    public void i_am_parked() {
        Assert.assertNotNull(UsersManager.INSTANCE.getCurrentUser().getSpotParked());
    }

    @Then("^The spot is occupied$")
    public void the_spot_is_occupied() {
        SpotsManager.INSTANCE.setSpotStatusToOccupied("TestSpot",false);
        Spot spot = SpotsManager.INSTANCE.getSpotFromId("TestSpot");
        Assert.assertEquals(1, spot.getStatus());
    }

    @Given("^I see a message asking if I want to add that spot to my favourits list spots$")
    public void i_see_a_message_asking_if_I_want_to_add_that_spot_to_my_favourits_list_spots() {
        onViewWithText(string(R.string.infoSpotIsFavourite)).isDisplayed();
    }

    @Given("^I press the button to add to my favourites$")
    public void i_press_the_button_to_add_to_my_favourites() {
        onViewWithId(R.id.btnAddToFavourites).click();
    }





}
