package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class US14FeatureSteps extends GreenCoffeeSteps {
    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I select the menu option \"([^\"]*)\" on dashboard authenticated$")
    public void i_select_the_menu_option_on_dashboard_authenticated(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @When("^I see the spots favourite list with the delete option$")
    public void i_see_the_spots_favourite_list_with_the_delete_option() {
        onViewWithId(R.id.spotsList).isDisplayed().isNotEmpty();
        onData(anything()).atPosition(0).onChildView(withId(R.id.btnDelete)).check(matches(withText(string(R.string.delete))));
    }

    @Given("^I see the spot on first position with information \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void i_see_the_spot_on_first_position_with_information(String arg1, String arg2, String arg3) {
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotId)).check(matches(withText(arg1)));
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotStatus)).check(matches(withText(arg2)));
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotRate)).check(matches(withText(arg3)));
    }

    @When("^I see the spot on second position with information \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void i_see_the_spot_on_second_position_with_information(String arg1, String arg2, String arg3) {
        onData(anything()).atPosition(1).onChildView(withId(R.id.spotId)).check(matches(withText(arg1)));
        onData(anything()).atPosition(1).onChildView(withId(R.id.spotStatus)).check(matches(withText(arg2)));
        onData(anything()).atPosition(1).onChildView(withId(R.id.spotRate)).check(matches(withText(arg3)));
    }

    @When("^I press the option delete on the first position of the list$")
    public void i_press_the_option_delete_on_the_first_position_of_the_list() {
        onData(anything()).atPosition(0).onChildView(withId(R.id.btnDelete)).perform(click());
    }

    @Then("^I see the spot on the first position is \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void i_see_the_spot_on_the_first_position_is(String arg1, String arg2, String arg3) {
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotId)).check(matches(withText(arg1)));
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotStatus)).check(matches(withText(arg2)));
        onData(anything()).atPosition(0).onChildView(withId(R.id.spotRate)).check(matches(withText(arg3)));
    }

    @Then("^I see an error message displayed saying \"([^\"]*)\"$")
    public void i_see_an_error_message_displayed_saying(String arg1) {
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(arg1)).inRoot(isDialog()).perform(click());
    }
}
