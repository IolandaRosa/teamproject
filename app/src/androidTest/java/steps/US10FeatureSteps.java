package steps;

import android.content.Intent;
import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.FindPreference;
import modelo.Spot;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US10FeatureSteps extends GreenCoffeeSteps {
    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Given("^I have BEST_RATED preferences$")
    public void i_have_BEST_RATED_preferences() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser().getUid(), FindPreference.BEST_RATED);
    }

    @When("^I select the menu option \"([^\"]*)\" on dashboard authenticated$")
    public void i_select_the_menu_option_on_dashboard_authenticated(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @Then("^A spot \"([^\"]*)\" is selected$")
    public void a_spot_is_selected(String arg1) {
        List<Spot> spots=new ArrayList<>();
        spots.add(new Spot("TESTE-1","A","1,2",0,3));
        spots.add(new Spot("TESTE-2","A","1,2",0,4));
        spots.add(new Spot("TESTE-3","A","1,2",0,2));
        spots.add(new Spot("TESTE-4","A","-1,5",0,1));
        Spot spot = DashboardAuthActivity.bestRatedSpotMethod(spots, 0);

        Assert.assertEquals(spot, new Spot("TESTE-2","A","1,2",0,4));
    }
}
