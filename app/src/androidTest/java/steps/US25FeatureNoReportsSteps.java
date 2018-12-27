package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import junit.framework.Assert;

import java.util.LinkedList;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.IncidentReport;
import modelo.IncidentsReportsManager;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US25FeatureNoReportsSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Given("^There's no reports$")
    public void there_s_no_reports() {
        IncidentsReportsManager.INSTANCE.setIncidentsList(new LinkedList<IncidentReport>());
        Assert.assertEquals(0, IncidentsReportsManager.INSTANCE.getIncidents().size());
    }

    @When("^I press the menu option \"([^\"]*)\"$")
    public void i_press_the_menu_option(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        sleep(500);
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^I see a message saying that the're no reports to show$")
    public void i_see_a_message_saying_that_the_re_no_reports_to_show() {
        onViewWithText(string(R.string.incidentsEmptyList)).isDisplayed();
    }

}
