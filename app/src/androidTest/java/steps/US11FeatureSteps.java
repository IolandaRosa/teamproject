package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;
import junit.framework.Assert;

import groupf.taes.ipleiria.spots.FindMeASpotActivity;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.UsersManager;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US11FeatureSteps extends GreenCoffeeSteps {

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

    @Then("^I see that the spot returned from my list is the closest spot$")
    public void i_see_that_the_spot_returned_from_my_list_is_the_closest_spot() {
        Spot bestRatedSpot = FindMeASpotActivity.getClosestSpot(SpotsManager.INSTANCE.getParkingSpotsTest());
        Assert.assertEquals(bestRatedSpot.getSpotId(),"A-1");
    }
}
