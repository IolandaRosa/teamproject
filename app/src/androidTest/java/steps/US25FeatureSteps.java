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
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US25FeatureSteps extends GreenCoffeeSteps {
    String description = "This is a test, this is a test, this is a test";

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I press the menu option \"([^\"]*)\"$")
    public void i_press_the_menu_option(String arg1) {

        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
    //    Espresso.onView(withText("Logout")).perform(scrollTo());
        Espresso.onView(withId(R.id.drawer_layout)).perform(swipeUp());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @When("^I see a list displayed with the part of the description and location of the incident$")
    public void i_see_a_list_displayed_with_the_part_of_the_description_and_location_of_the_incident() {
        onViewWithId(R.id.incidentsList).isDisplayed();
        onViewWithText(description.substring(0, 20) + "...").isDisplayed();
    }

    @When("^I press a position on the list$")
    public void i_press_a_position_on_the_list() {
        onViewWithText("TestSpot").click();
    }

    @Then("^I am on the report details screen$")
    public void i_am_on_the_report_details_screen() {
        onViewWithId(R.id.txtReportDetailDescription).isDisplayed();
        onViewWithId(R.id.txtReportDetailSpot).isDisplayed();
        onViewWithId(R.id.txtReportDetailLocation).isNotDisplayed();
    }

    @Then("^I see the total description and location of report displayed$")
    public void i_see_the_total_description_and_location_of_report_displayed() {
        onViewWithId(R.id.txtReportDetailDescription).contains(description);
        onViewWithId(R.id.txtReportDetailSpot).contains("TestSpot");
    }

}
