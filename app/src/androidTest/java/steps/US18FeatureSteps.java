package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.UsersManager;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US18FeatureSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Given("^I am parked$")
    public void i_am_parked() {
        Assert.assertNotNull(UsersManager.INSTANCE.getCurrentUser().getSpotParked());
    }

    @Given("^I select the option \"([^\"]*)\" on the menu$")
    public void i_select_the_option_on_the_menu(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(1000);
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Given("^I see a dialog asking if I want to leave$")
    public void i_see_a_dialog_asking_if_I_want_to_leave() {
        onViewWithText(string(R.string.askUserIsLeavingTheSpot)).isDisplayed();
    }

    @Given("^I select the option \"([^\"]*)\"$")
    public void i_select_the_option(String arg1) {
        onViewWithText(arg1).click();
    }

    @Given("^I see an ecran where I can rate the spot, add to my favourites and close this ecran$")
    public void i_see_an_ecran_where_I_can_rate_the_spot_add_to_my_favourites_and_close_this_ecran() {
        onViewWithId(R.id.ratingBar).isDisplayed();
        onViewWithId(R.id.btnSendRate).isDisplayed();
        onViewWithId(R.id.btnAddToFavourites).isDisplayed();
        onViewWithId(R.id.btnClose).isDisplayed();
    }

    @Then("^I press the close button$")
    public void i_press_the_close_button() {
        onViewWithId(R.id.btnClose).click();
    }

    @Then("^I am on the auth dashboard$")
    public void i_am_on_the_auth_dashboard() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
    }

    @Then("^I am not parked$")
    public void i_am_not_parked() {
        Assert.assertNull(UsersManager.INSTANCE.getCurrentUser().getSpotParked());
    }

    @Then("^The spot is free$")
    public void the_spot_is_free() {
        Spot spot = SpotsManager.INSTANCE.getSpotFromId("TestSpot");
        Assert.assertEquals(0, spot.getStatus());
    }

   /* @Then("^I am parked$")
    public void i_am_parked() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    } */

    @Then("^The spot is occupied$")
    public void the_spot_is_occupied() {
        Spot spot = SpotsManager.INSTANCE.getSpotFromId("TestSpot");
        Assert.assertEquals(1, spot.getStatus());
    }




}
