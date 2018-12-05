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
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US12FeatureAllSpotsOccupiedSteps extends GreenCoffeeSteps {

    @Given("^I am authenticated user$")
    public void i_am_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I select the option Find Me A Spot on dashboard auth menu$")
    public void i_select_the_option_Find_Me_A_Spot_on_dashboard_auth_menu() {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText("Find me a spot")).perform(click());
    }

    @Then("^I see an error message displayed saying \"([^\"]*)\"$")
    public void i_see_an_error_message_displayed_saying(String arg1) {
        sleep(100);
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(arg1)).inRoot(isDialog()).perform(click());
    }
}
