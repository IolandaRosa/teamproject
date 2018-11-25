package steps;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US6FeatureSteps extends GreenCoffeeSteps {
    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I am in the authenticated dashboard page$")
    public void i_am_in_the_authenticated_dashboard_page() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
        onViewWithId(R.id.drawer_layout).isDisplayed();
    }

    @When("^I press the hamburguer button$")
    public void i_press_the_hamburguer_button() {
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
    }

    @When("^I press the logout button$")
    public void i_press_the_logout_button() {
        //Espresso.onView(withText("Logout")).perform(click());
        String[] options = InstrumentationRegistry.getTargetContext().getResources().getStringArray(R.array.dashboardIems);
        Espresso.onView(withText(options[6])).perform(click());
        sleep(2000);
    }

    @When("^I am in the anonymous dashboard page$")
    public void i_am_in_the_anonymous_dashboard_page() {
        sleep(200);
        onViewWithId(R.id.btnSignup).isDisplayed();
         onViewWithId(R.id.spinner).doesNotExist();
         onViewWithId(R.id.drawer_layout).doesNotExist();
         onViewWithText(string(R.string.title_activity_maps)).isDisplayed();
    }

    @When("^I am an anonymous user$")
    public void i_am_an_anonymous_user() {
        Assert.assertNull(FirebaseAuth.getInstance().getCurrentUser());
    }


}

