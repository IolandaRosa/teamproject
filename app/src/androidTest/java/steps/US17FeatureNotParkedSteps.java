package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.UsersManager;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US17FeatureNotParkedSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Given("^I am not parked$")
    public void i_am_not_parked() {
        Assert.assertNull(UsersManager.INSTANCE.getCurrentUser().getSpotParked());
    }

    @Given("^I select the option \"([^\"]*)\" on the menu$")
    public void i_select_the_option_on_the_menu(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(1000);
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I see a message saying that I am not parked$")
    public void i_see_a_message_saying_that_I_am_not_parked() {
        onViewWithText(string(R.string.mySpotErrorUserNotParked)).isDisplayed();
    }

}
