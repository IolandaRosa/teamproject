package steps;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import junit.framework.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class US22FeatureSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I press the menu option \"([^\"]*)\"$")
    public void i_press_the_menu_option(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        onView(withId(R.id.navList)).perform(swipeUp());
        onView(allOf( withText(arg1),hasSibling(withText(arg1)),isDisplayed()))
                .perform(scrollTo(), click());
    }

    @When("^I insert the initial date value \"([^\"]*)\"$")
    public void i_insert_the_initial_date_value(String arg1) {
        closeKeyboard();
        onViewWithId(R.id.editTextInitDate).type(arg1);
    }

    @When("^I insert the final date value \"([^\"]*)\"$")
    public void i_insert_the_final_date_value(String arg1) {
        closeKeyboard();
        onViewWithId(R.id.editTextFinalDate).type(arg1);
    }

    @When("^I press the button with text \"([^\"]*)\"$")
    public void i_press_the_button_with_text(String arg1) {
        onViewWithId(R.id.btnDisplayGraph).isDisplayed().check(matches(withText(arg1))).click();
    }

    @Then("^I see the graphical information displayed$")
    public void i_see_the_graphical_information_displayed() {
        onViewWithId(R.id.graph).isDisplayed();
    }

    @When("^I press the button to see graphical information$")
    public void i_press_the_button_to_see_graphical_information() {

    }

    @Then("^I see an error message saying \"([^\"]*)\"$")
    public void i_see_an_error_message_saying(String arg1) {

    }
}
