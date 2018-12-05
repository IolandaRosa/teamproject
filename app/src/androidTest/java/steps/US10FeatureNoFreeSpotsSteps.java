package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import groupf.taes.ipleiria.spots.FindMeASpotActivity;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.Spot;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

public class US10FeatureNoFreeSpotsSteps extends GreenCoffeeSteps {

    private List<Spot> spots = new ArrayList<>();

    private void populateSpots() {
        spots.add(new Spot("TESTE-1", "A", "1,2", 1, 3));
        spots.add(new Spot("TESTE-2", "A", "1,2", 1, 4));
        spots.add(new Spot("TESTE-3", "A", "1,2", 1, 2));
        spots.add(new Spot("TESTE-4", "A", "-1,5", 1, 4));
    }

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        populateSpots();
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Given("^I select the menu option \"([^\"]*)\" on dashboard authenticated$")
    public void i_select_the_menu_option_on_dashboard_authenticated(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @When("^I have select the park 'A' on spinner option$")
    public void i_have_select_the_park_A_on_spinner_option() {
        onViewWithId(R.id.spinner).click();
        onData(is("Park Campus D")).perform(click());
    }

    @When("^I have no free spots avaiable$")
    public void i_have_no_free_spots_avaiable() {
        for (Spot s : spots) {
            Assert.assertEquals(1, s.getStatus());
        }

        Assert.assertEquals(FindMeASpotActivity.getBestRatedSpot(spots),null);
    }

    @When("^I select the menu option \"([^\"]*)\" dashboard authenticated$")
    public void i_select_the_menu_option_dashboard_authenticated(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I see the error message says \"([^\"]*)\"$")
    public void i_see_the_error_message_says(String arg1) {
        sleep(100);
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(arg1)).inRoot(isDialog()).perform(click());
    }
}
