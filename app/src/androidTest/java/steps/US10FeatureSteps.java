package steps;

import android.content.Intent;
import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.List;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.FindPreference;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.UsersManager;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

public class US10FeatureSteps extends GreenCoffeeSteps {

    private List<Spot> spots=new ArrayList<>();

    private void populateSpots(){
        spots.add(new Spot("TESTE-1","A","1,2",0,3));
        spots.add(new Spot("TESTE-2","A","1,2",1,4));
        spots.add(new Spot("TESTE-3","A","1,2",0,2));
        spots.add(new Spot("TESTE-4","A","-1,5",0,4));
    }

    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        populateSpots();
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I select the menu option \"([^\"]*)\" on dashboard authenticated$")
    public void i_select_the_menu_option_on_dashboard_authenticated(String arg1) {
        onViewWithId(R.id.drawer_layout).isDisplayed();
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        Espresso.onView(withText(arg1)).perform(click());
    }

    @When("^I have select the park 'A' on spinner option$")
    public void i_have_select_the_park_A_on_spinner_option() {
        onViewWithId(R.id.spinner).click();
        onData(is("Park Campus A")).perform(click());
    }

    @Then("^A spot \"([^\"]*)\" is selected$")
    public void a_spot_is_selected(String arg1) {
        Spot spot = DashboardAuthActivity.getBestRatedSpot(spots);

        Assert.assertEquals("TESTE-4", spot.getSpotId());
    }
}
