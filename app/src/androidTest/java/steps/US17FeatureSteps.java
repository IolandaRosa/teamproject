package steps;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;

import org.junit.Assert;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.UsersManager;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US17FeatureSteps extends GreenCoffeeSteps {

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

    @Then("^I am on the dashboard$")
    public void i_am_on_the_dashboard() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
    }

    @Then("^I see only my spot marked on the map$")
    public void i_see_only_my_spot_marked_on_the_map() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject freeMarker = device.findObject(new UiSelector().descriptionContains("TestSpot1"));
        Assert.assertFalse(freeMarker.exists());
        UiObject userSpotMarker = device.findObject(new UiSelector().descriptionContains("TestSpot"));
        Assert.assertTrue(userSpotMarker.exists());

    }

}
