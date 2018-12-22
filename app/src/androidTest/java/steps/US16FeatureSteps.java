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
import modelo.SpotsManager;
import modelo.UsersManager;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

public class US16FeatureSteps extends GreenCoffeeSteps {

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
        onViewWithText(string(R.string.infoForUserParkManually)).isDisplayed();
        onViewWithText(string(R.string.OK)).click();
    }

    @Then("^I select a marker on the map$")
    public void i_select_a_marker_on_the_map() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("TestSpot"));
        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {

        }
    }


    @Then("^I see a message asking \"([^\"]*)\"$")
    public void i_see_a_message_asking(String arg1) {
        onViewWithText(string(R.string.msgAskUserIfWantToPark)).isDisplayed();
    }

    @Then("^I select other marker on the map$")
    public void i_select_other_marker_on_the_map() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("TestSpot1"));

        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {
            Assert.assertTrue(marker.exists());
        }
    }


    @Then("^I select select the option \"([^\"]*)\"$")
    public void i_select_select_the_option(String arg1) {
        onViewWithText(arg1).click();
    }


    @Then("^I am on the dashboard auth$")
    public void i_am_on_the_dashboard_auth() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
    }


    @Then("^The spot status is occupied$")
    public void the_spot_status_is_occupied() {
        List<Spot> parkingSpotsA = SpotsManager.INSTANCE.getParkingSpotsA();
        Spot spotTest = null;
        for (Spot s : parkingSpotsA) {
            if (s.getSpotId().equalsIgnoreCase("TestSpot")) {
                spotTest = s;
            }
        }

        Assert.assertEquals(1, spotTest.getStatus());
    }


    @Then("^The spot status is free$")
    public void the_spot_status_is_free() {
        List<Spot> parkingSpotsA = SpotsManager.INSTANCE.getParkingSpotsA();
        Spot spotTest = null;
        for (Spot s : parkingSpotsA) {
            if (s.getSpotId().equalsIgnoreCase("TestSpot1")) {
                spotTest = s;
            }
        }

        Assert.assertEquals(0, spotTest.getStatus());
    }





}
