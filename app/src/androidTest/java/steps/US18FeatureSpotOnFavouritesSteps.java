package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;

import org.junit.Assert;

import java.util.List;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.UsersManager;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US18FeatureSpotOnFavouritesSteps extends GreenCoffeeSteps {

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

    @Given("^The spot is already on my favourites list$")
    public void the_spot_is_already_on_my_favourites_list() {
        Spot spot = SpotsManager.INSTANCE.getSpotFromId("TestSpot");
        List<Spot> spotsList = UsersManager.INSTANCE.getCurrentUser().getFavouriteSpots();
        Assert.assertTrue(spotsList.contains(spot));
    }

    @Then("^I see an ecran where I can rate the spot and close this ecran$")
    public void i_see_an_ecran_where_I_can_rate_the_spot_and_close_this_ecran() {
        onViewWithId(R.id.ratingBar).isDisplayed();
        onViewWithId(R.id.btnSendRate).isDisplayed();
        onViewWithId(R.id.btnClose).isDisplayed();
    }

    @Then("^I don't see the button to add to my favourites$")
    public void i_don_t_see_the_button_to_add_to_my_favourites() {
        onViewWithId(R.id.btnAddToFavourites).isNotDisplayed();
    }

    @Then("^I see a message saying that the spot is already on my favourites list$")
    public void i_see_a_message_saying_that_the_spot_is_already_on_my_favourites_list() {
        onViewWithText(string(R.string.infoSpotIsFavourite)).isDisplayed();
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



}
