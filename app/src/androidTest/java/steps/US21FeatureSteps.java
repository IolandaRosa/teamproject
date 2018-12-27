package steps;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import junit.framework.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class US21FeatureSteps extends GreenCoffeeSteps {
    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I press the menu option \"([^\"]*)\"$")
    public void i_press_the_menu_option(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
        onView(withId(R.id.navList)).perform(swipeUp());
        onView(allOf( withText(arg1),hasSibling(withText(arg1)),isDisplayed()))
                .perform(scrollTo(), click());
    }

    @Then("^I am in the statistics screen$")
    public void i_am_in_the_statistics_screen() {
        onViewWithText(R.string.findMeASpotTitle).isDisplayed();
        onViewWithText(R.string.best_rated).isDisplayed();
        onViewWithText(R.string.closerLocation).isDisplayed();
        //onViewWithText(R.string.myFavourites).isDisplayed();
        onViewWithText(R.string.occupationRateTittle).isDisplayed();
        onViewWithId(R.id.btnDisplayDataInsertionArea).isDisplayed();
    }

    @Then("^I see the performance time and statistics displayed$")
    public void i_see_the_performance_time_and_statistics_displayed() {
        onViewWithId(R.id.txtBestRatedTime).isDisplayed().isNotEmpty();
        //onViewWithText(R.id.txtMyFavouritesTime).isDisplayed().isNotEmpty();
        onViewWithId(R.id.txtCloserLocationTime).isDisplayed().isNotEmpty();
        onViewWithId(R.id.txtOccupationRate).isDisplayed().isNotEmpty();
        String[] split = onViewWithId(R.id.txtOccupationRate).text().split("%");
        String[] split1 = split[0].split("-");
        Assert.assertTrue(Double.parseDouble(split1[1])>=0 && Double.parseDouble(split1[1])<=100);

    }

    @When("^I press the Profile$")
    public void i_press_the_Profile() {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
        onView(withText("Profile")).perform(click());
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
        onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
        onView(withId(R.id.navList)).perform(swipeUp());
        onView(withText("Statistics")).perform(click());
    }

    @When("^I press the Change my password$")
    public void i_press_the_Change_my_password() {
        onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
        onView(withId(R.id.navList)).perform(swipeUp());
        onView(withText("Change my password")).perform(click());
    }

    @When("^I press the \"([^\"]*)\"$")
    public void i_press_the(String arg1) {
        onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
        onView(withText(arg1)).perform(click());
    }

    @When("^I press that button$")
    public void i_press_that_button() {
        onViewWithId(R.id.btnUpdateMyProfile).click();
    }

    @When("^I press the Occupation Rate During Time$")
    public void i_press_the_Occupation_Rate_During_Time() {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        onView(withId(R.id.navList)).perform(swipeUp());
        onView(allOf( withText("Occupation Rate During Time"),hasSibling(withText("Occupation Rate During Time")),isDisplayed()))
                .perform(scrollTo(), click());
    }

}
