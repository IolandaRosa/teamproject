package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import groupf.taes.ipleiria.spots.FindMeASpotActivity;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.Spot;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US11FeatureSteps extends GreenCoffeeSteps {

    private List<Spot> spots = new ArrayList<>();

    private void populateSpots() {
        spots.add(new Spot("A-1", "A", "1,5", 0, 3,0));
        spots.add(new Spot("A-2", "A", "1,2", 1, 4,0));
        spots.add(new Spot("A-3", "A", "1,2", 0, 2,0));
        spots.add(new Spot("A-4", "A", "-1,5", 0, 4,0));
    }

    @Given("^I am authenticated user$")
    public void i_am_authenticated_user() {
        populateSpots();
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I select the option Find Me A Spot on dashboard auth menu$")
    public void i_select_the_option_Find_Me_A_Spot_on_dashboard_auth_menu() {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText("Find me a spot")).perform(click());
    }

    @Then("^I see that the spot returned from my list is the closest spot$")
    public void i_see_that_the_spot_returned_from_my_list_is_the_closest_spot() {
        Spot closerSpot = FindMeASpotActivity.getCloserSpot(1, 2, spots);

        Assert.assertEquals( "A-3",closerSpot.getSpotId());
    }
}
