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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Given("^I see a dialog asking if I want to leave$")
    public void i_see_a_dialog_asking_if_I_want_to_leave() {
        onView(withText(string(R.string.askUserIsLeavingTheSpot))).inRoot(isDialog()).check(matches(isDisplayed()));
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

    @Given("^I press a star$")
    public void i_press_a_star() {
        onViewWithId(R.id.ratingBar).click();
    }

    @Given("^The button to send rate$")
    public void the_button_to_send_rate() {
        onViewWithId(R.id.btnSendRate).click();
    }

    @Then("^I see a text saying that the rate was sended$")
    public void i_see_a_text_saying_that_the_rate_was_sended() {
        onViewWithText(string(R.string.infoRateSended)).click();
    }

    @Then("^I press the button to add to my favourites$")
    public void i_press_the_button_to_add_to_my_favourites() {
        onViewWithId(R.id.btnAddToFavourites).click();
    }

    @Then("^I see a message saying that the spot is on my favourites list$")
    public void i_see_a_message_saying_that_the_spot_is_on_my_favourites_list() {
        onViewWithText(string(R.string.infoSpotIsFavourite)).isDisplayed();
    }

    @Then("^I press the close button$")
    public void I_press_the_close_button() {
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

    @Then("^The spot is occupied$")
    public void the_spot_is_occupied() {
        Spot spot = SpotsManager.INSTANCE.getSpotFromId("TestSpot");
        Assert.assertEquals(1, spot.getStatus());
    }




}
