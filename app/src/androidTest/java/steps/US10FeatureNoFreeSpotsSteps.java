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
import static org.hamcrest.Matchers.is;

public class US10FeatureNoFreeSpotsSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I have select the park 'D' on spinner option$")
    public void i_have_select_the_park_D_on_spinner_option() {
        onViewWithId(R.id.spinner).click();
        onData(is("Park Campus D")).perform(click());
    }

    @When("^I select the menu option \"([^\"]*)\" dashboard authenticated$")
    public void i_select_the_menu_option_dashboard_authenticated(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I see the error message says \"([^\"]*)\"$")
    public void i_see_the_error_message_says(String arg1) {
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(arg1)).inRoot(isDialog()).perform(click());
    }
}
