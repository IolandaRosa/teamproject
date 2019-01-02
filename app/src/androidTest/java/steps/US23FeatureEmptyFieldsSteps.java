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
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

public class US23FeatureEmptyFieldsSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I press the menu option \"([^\"]*)\"$")
    public void i_press_the_menu_option(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
        Espresso.onView(withId(R.id.drawer_layout)).perform(swipeUp());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @When("^I press the save button$")
    public void i_press_the_save_button() {
        Espresso.closeSoftKeyboard();
        onViewWithId(R.id.btnSaveReport).click();
    }

    @Then("^I see a error saying that description can't be empty$")
    public void i_see_a_error_saying_that_description_can_t_be_empty() {
        onViewWithId(R.id.editTxtDescription).hasErrorText(string(R.string.errorReportDescriptionEmpty));
    }

    @When("^I insert a description on the description field$")
    public void i_insert_a_description_on_the_description_field() {
        onViewWithId(R.id.editTxtDescription).type("This is a description");
        Espresso.closeSoftKeyboard();
    }


    @Then("^I see a error saying that I need to put a location$")
    public void i_see_a_error_saying_that_I_need_to_put_a_location() {
        onViewWithText(string(R.string.errorReportLocationNotPut)).isDisplayed();
    }


}
