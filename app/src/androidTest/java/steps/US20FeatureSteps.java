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

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US20FeatureSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I press the menu option \"([^\"]*)\" on dashboard auth screen$")
    public void i_press_the_menu_option_on_dashboard_auth_screen(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I see the statistic screen displayed$")
    public void i_see_the_statistic_screen_displayed() {
        onViewWithText(R.string.totalLoggedUsers).isDisplayed();
        onViewWithText(R.string.totalParkedUsers).isDisplayed();
        onViewWithText(R.string.totalRegistered).isDisplayed();
        onViewWithText(R.string.topBestRated).isDisplayed();
        onViewWithText(R.string.topMostParked).isDisplayed();
    }

    @Then("^I confirm that the value of registered users is at least (\\d+)$")
    public void i_confirm_that_the_value_of_registered_users_is_at_least(int arg1) {
        onViewWithId(R.id.txtTotalRegisteredUsers).isDisplayed().isNotEmpty();
        Assert.assertTrue(Integer.parseInt(onViewWithId(R.id.txtTotalRegisteredUsers).text())>=1);
    }

    @Then("^The value of logged users is at least (\\d+)$")
    public void the_value_of_logged_users_is_at_least(int arg1) {
        onViewWithId(R.id.txtTotalLoggedUsers).isDisplayed().isNotEmpty();
        Assert.assertTrue(Integer.parseInt(onViewWithId(R.id.txtTotalLoggedUsers).text())>=1);
    }

    @Then("^The value of total occupied spots is at least (\\d+)$")
    public void the_value_of_total_occupied_spots_is_at_least(int arg1) {
        onViewWithId(R.id.textTotalParkedUsers).isDisplayed().isNotEmpty();
        Assert.assertTrue(Integer.parseInt(onViewWithId(R.id.textTotalParkedUsers).text())>=0);
    }

    @Then("^The top (\\d+) most parking and best rated spots is displayed$")
    public void the_top_most_parking_and_best_rated_spots_is_displayed(int arg1) {
        onViewWithId(R.id.textTopBestRated).isDisplayed().isNotEmpty();
        String[] split = onViewWithId(R.id.textTopBestRated).text().split("\n");
        Assert.assertTrue(split.length==arg1);
        onViewWithId(R.id.textTopMostParked).isDisplayed().isNotEmpty();
        split = onViewWithId(R.id.textTopMostParked).text().split("\n");
        Assert.assertTrue(split.length==arg1);
    }
}
