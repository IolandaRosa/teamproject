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

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class US13FeatureWithFavouritesListSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticate user$")
    public void i_am_an_authenticate_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I am in dashboard authenticated screen$")
    public void i_am_in_dashboard_authenticated_screen() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
        onViewWithId(R.id.drawer_layout).isDisplayed();
    }

    @Then("^I open the menu$")
    public void i_open_the_menu() {
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
    }

    @Given("^I press the \"([^\"]*)\" option$")
    public void i_press_the_option(String arg1) {
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I see the favourite spots list displayed$")
    public void i_see_the_favourite_spots_list_displayed() {
        onViewWithId(R.id.spotsList).isDisplayed().isNotEmpty();
    }

    @When("^I am in the profile screen$")
    public void i_am_in_the_profile_screen() {
        onViewWithId(R.id.btnMyPreferences).isDisplayed().check(matches(withText(R.string.btnMyPreferences)));
        onViewWithId(R.id.btnFavouriteSpots).isDisplayed().check(matches(withText(R.string.btnFavouriteSpots)));
        onViewWithId(R.id.btnUpdateMyProfile).isDisplayed().check(matches(withText(R.string.btnUpdateMyProfile)));
        onViewWithText(R.string.name).isDisplayed();
        onViewWithId(R.id.txtViewName).isDisplayed();
        onViewWithText(R.string.email).isDisplayed();
        onViewWithId(R.id.txtViewEmail).isDisplayed();
        onViewWithText(R.string.findMeASpotPreference).isDisplayed();
    }

    @When("^I click the button \"([^\"]*)\"$")
    public void i_click_the_button(String arg1) {
        onViewWithId(R.id.btnFavouriteSpots).click();
    }

    @Then("^I see the name A-(\\d+), park D, status Free and the rate (\\d+) displayed on the list$")
    public void i_see_the_name_A_park_D_status_Free_and_the_rate_displayed_on_the_list(int arg1, int arg2) {
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotId)).check(matches(withText("Name: A-"+Integer.toString(arg1))));
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotPark)).check(matches(withText("Park: D")));
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotStatus)).check(matches(withText("Status: Free    ")));
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotRate)).check(matches(withText("Rate: "+Integer.toString(arg2))));
    }

    @Then("^I see the name A-(\\d+), park D, status Occupied and the rate (\\d+) displayed on the list$")
    public void i_see_the_name_A_park_D_status_Occupied_and_the_rate_displayed_on_the_list(int arg1, int arg2) {
        onData(anything()).atPosition(1).onChildView(withId(R.id.spotId)).check(matches(withText("Name: A-"+Integer.toString(arg1))));
        onData(anything()).atPosition(1).onChildView(withId(R.id.spotPark)).check(matches(withText("Park: D")));
        onData(anything()).atPosition(1).onChildView(withId(R.id.spotStatus)).check(matches(withText("Status: Occupied")));
        onData(anything()).atPosition(1).onChildView(withId(R.id.spotRate)).check(matches(withText("Rate: "+Integer.toString(arg2))));
    }
}
