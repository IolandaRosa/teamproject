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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US21FeatureSteps extends GreenCoffeeSteps {
    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I press the menu option \"([^\"]*)\"$")
    public void i_press_the_menu_option(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I am in the statistics screen$")
    public void i_am_in_the_statistics_screen() {
        onViewWithText(R.string.findMeASpotTitle).isDisplayed();
        onViewWithText(R.string.best_rated).isDisplayed();
        onViewWithText(R.string.closerLocation).isDisplayed();
        onViewWithText(R.string.myFavourites).isDisplayed();
        onViewWithText(R.string.occupationRateTittle).isDisplayed();
        onViewWithId(R.id.btnDisplayDataInsertionArea).isDisplayed();
    }

    @Then("^I see the performance time and statistics displayed$")
    public void i_see_the_performance_time_and_statistics_displayed() {
        onViewWithId(R.id.txtBestRatedTime).isDisplayed().isNotEmpty();
        onViewWithText(R.id.txtMyFavouritesTime).isDisplayed().isNotEmpty();
        onViewWithId(R.id.txtCloserLocationTime).isDisplayed().isNotEmpty();
        onViewWithId(R.id.txtOccupationRate).isDisplayed().isNotEmpty();
        String[] split = onViewWithId(R.id.txtOccupationRate).text().split("%");
        Assert.assertTrue(Integer.parseInt(split[0])>=0 && Integer.parseInt(split[0])<=100);

    }

    @When("^I press the Profile$")
    public void i_press_the_Profile() {
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText("Profile")).perform(click());
    }

    @When("^I see the button \"([^\"]*)\"$")
    public void i_see_the_button(String arg1) {
        onViewWithText(arg1).isDisplayed();
    }

    @When("^I press the button$")
    public void i_press_the_button() {
        onViewWithId(R.id.btnShowPerformance).click();
    }

    @When("^I press the Statistics$")
    public void i_press_the_Statistics() {
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText("Statistics")).perform(click());
    }

    @When("^I press the Change my password$")
    public void i_press_the_Change_my_password() {
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText("Change my password")).perform(click());
    }

    @When("^I press the \"([^\"]*)\"$")
    public void i_press_the(String arg1) {
        onViewWithText(arg1).click();
    }

    @When("^I press that button$")
    public void i_press_that_button() {
        onViewWithId(R.id.btnShowPerformance).click();
    }
}
