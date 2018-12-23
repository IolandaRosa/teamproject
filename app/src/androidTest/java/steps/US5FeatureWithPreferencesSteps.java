package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import junit.framework.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US5FeatureWithPreferencesSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I am in the dashboard authenticated screen$")
    public void i_am_in_the_dashboard_authenticated_screen() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
        onViewWithId(R.id.drawer_layout).isDisplayed();
    }

    @When("^I press the My Profile button$")
    public void i_press_the_My_Profile_button() {
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText("Profile")).perform(click());
    }

    @When("^I am in the Profile screen$")
    public void i_am_in_the_Profile_screen() {
        onViewWithId(R.id.btnMyPreferences).isDisplayed().check(matches(withText(R.string.btnMyPreferences)));
        onViewWithId(R.id.btnFavouriteSpots).isDisplayed().check(matches(withText(R.string.btnFavouriteSpots)));
        onViewWithId(R.id.btnUpdateMyProfile).isDisplayed().check(matches(withText(R.string.btnUpdateMyProfile)));
    }

    @When("^The name matches with \"([^\"]*)\"$")
    public void the_name_matches_with(String arg1) {
        onViewWithText(R.string.name).isDisplayed();
        onViewWithId(R.id.txtViewName).isDisplayed().check(matches(withText(arg1)));
    }

    @When("^The email mathces with \"([^\"]*)\"$")
    public void the_email_mathces_with(String arg1) {
        onViewWithText(R.string.email).isDisplayed();
        onViewWithId(R.id.txtViewEmail).isDisplayed().check(matches(withText(arg1)));
    }

    @When("^The Find Me a Spot Preferences is displayed$")
    public void the_Find_Me_a_Spot_Preferences_is_displayed() {
        onViewWithText(R.string.findMeASpotPreference).isDisplayed();
    }

    @Then("^The preferences shows \"([^\"]*)\"$")
    public void the_preferences_shows(String arg1) {
        onViewWithId(R.id.textViewPreference).isDisplayed().check(matches(withText(arg1)));
    }
}
