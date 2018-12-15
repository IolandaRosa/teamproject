package steps;

import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.RootMatchers;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;


import org.hamcrest.Matcher;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.Spot;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US16FeatureSteps extends GreenCoffeeSteps {
    private List<Spot> spots = new ArrayList<>();

    private void populateSpots() {
        spots.add(new Spot("A-1", "A", "1,5", 0, 3));
        spots.add(new Spot("A-2", "A", "1,2", 0, 2));
        spots.add(new Spot("A-3", "A", "-1,5", 0, 4));
    }


    @Given("^I am authenticated user$")
    public void i_am_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I press the \"([^\"]*)\" on the menu$")
    public void i_press_the_on_the_menu(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(1000);
        Espresso.onView(withText(arg1)).perform(click());
    }


    @Then("^I see a dialog box saying \"([^\"]*)\"$")
    public void i_see_a_dialog_box_saying(String arg1) {
       // sleep(1000);
        onViewWithText(string(R.string.infoForUserParkManually)).isDisplayed();
       /*Espresso.onView(withText(string(R.string.infoForUserParkManually)))
            .inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()));*/
        onViewWithText(string(R.string.OK)).click();

      /*  Espresso.onView(withText(string(R.string.OK)))
                .inRoot(RootMatchers.isDialog()).perform(click()); */
    }

    @Then("^I select a marker on the map$")
    public void i_select_a_marker_on_the_map() {
    /*    sleep(1000);
        Espresso.onView(withContentDescription("A-1")).perform(click()); */
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("A-2"));
        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {

        }

    }

    @Then("^I see a message asking \"([^\"]*)\"$")
    public void i_see_a_message_asking(String arg1) {
        onViewWithText(string(R.string.msgAskUserIfWantToPark)).isDisplayed();
    }

    @Then("^I select select the option \"([^\"]*)\"$")
    public void i_select_select_the_option(String arg1) {
        onViewWithText(string(R.string.Yes)).click();
    }











}
