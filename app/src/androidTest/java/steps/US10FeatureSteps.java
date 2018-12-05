package steps;

import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import groupf.taes.ipleiria.spots.FindMeASpotActivity;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.Spot;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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


    @When("^There are a list of spots not empy$")
    public void there_are_a_list_of_spots_not_empy() {
        Assert.assertTrue(!spots.isEmpty());
    }

    @Then("^A find a spot by best rated preference returns the spot \"([^\"]*)\"$")
    public void a_find_a_spot_by_best_rated_preference_returns_the_spot(String arg1) {
        Spot spot = FindMeASpotActivity.getBestRatedSpot(spots);

        Assert.assertEquals(arg1, spot.getSpotId());
    }


}
