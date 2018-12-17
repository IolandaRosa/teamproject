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

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

public class US16FeatureNoFreeSpotsSteps extends GreenCoffeeSteps {

    @Given("^I am authenticated user$")
    public void i_am_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I select the Park D$")
    public void i_select_the_Park_D() {
        onViewWithId(R.id.spinner).click();
        onData(is("Park Campus D")).perform(click());
    }

    @When("^I press the \"([^\"]*)\" on the menu$")
    public void i_press_the_on_the_menu(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(1000);
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I see a dialog box saying that there's no free spots$")
    public void i_see_a_dialog_box_saying_that_there_s_no_free_spots() {
        onViewWithText(string(R.string.noFreeSpots)).isDisplayed();
    }


}
