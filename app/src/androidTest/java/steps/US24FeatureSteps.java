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

public class US24FeatureSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I press the menu option \"([^\"]*)\"$")
    public void i_press_the_menu_option(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withId(R.id.drawer_layout)).perform(swipeUp());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @When("^I insert the description on the description field$")
    public void i_insert_the_description_on_the_description_field() {
        onViewWithId(R.id.editTxtDescription).type("This is a description");
        Espresso.closeSoftKeyboard();
    }

    @When("^I see a button to get my current location and to choose a park and spot$")
    public void i_see_a_button_to_get_my_current_location_and_to_choose_a_park_and_spot() {
        onViewWithId(R.id.btnGetCurrentLocation).isDisplayed();
        onViewWithId(R.id.btnChooseParkSpot).isDisplayed();
    }

    @When("^I press the button to get my current location$")
    public void i_press_the_button_to_get_my_current_location() {
        onViewWithId(R.id.btnGetCurrentLocation).click();
    }

    @When("^I don't see the button to choose a park and a spot$")
    public void i_don_t_see_the_button_to_choose_a_park_and_a_spot() {
        onViewWithId(R.id.btnChooseParkSpot).isNotDisplayed();
    }

    @When("^I see a message saying that my current location was obtained$")
    public void i_see_a_message_saying_that_my_current_location_was_obtained() {
        onViewWithText(string(R.string.infoReportGotUserLocation)).isDisplayed();
    }

    @When("^I press the button to upload a image$")
    public void i_press_the_button_to_upload_a_image() {
       onViewWithId(R.id.btnUploadPhoto).click();
    }
}
