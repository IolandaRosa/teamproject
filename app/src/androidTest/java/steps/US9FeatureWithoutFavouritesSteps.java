package steps;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.User;
import modelo.UsersManager;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US9FeatureWithoutFavouritesSteps extends GreenCoffeeSteps {
    private User currentUser;

    @Given("^I am an authenticated user $")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Given("^I don't have a Find me a Spot preference$")
    public void i_don_t_have_a_Find_me_a_Spot_preference() {
        currentUser = UsersManager.INSTANCE.getCurrentUser();
        Assert.assertNull(currentUser.getFindPreference());
    }

    @Given("^I don't have favourites spots$")
    public void i_don_t_have_favourites_spots() {
        Assert.assertEquals(0, currentUser.getFavouriteSpots().size());
    }

    @When("^I press the Find Me a Spot on the menu $")
    public void i_press_the_Find_Me_a_Spot_on_the_menu() {
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        String[] options = InstrumentationRegistry.getTargetContext().getResources().getStringArray(R.array.dashboardIems);
        Espresso.onView(withText(options[1])).perform(click());
    }

    @Then("^I see buttons with the options The best spot available and The spot closer to me$")
    public void i_see_buttons_with_the_options_The_best_spot_available_and_The_spot_closer_to_me() {
        onViewWithId(R.id.btnBestRatedSpot).isDisplayed();
        onViewWithId(R.id.btnCloserToMe).isDisplayed();
        onViewWithId(R.id.btnBestRatedSpot).contains(string(R.string.theBestRatedSpotAvailable));
        onViewWithId(R.id.btnCloserToMe).contains(string(R.string.theSpotCloserToMe));
    }

    @Then("^I don't see the button with the option One of my favourite spot available$")
    public void i_don_t_see_the_button_with_the_option_One_of_my_favourite_spot_available() {
        onViewWithId(R.id.btnOneOfFavourites).isNotDisplayed();
        onViewWithId(R.id.btnOneOfFavourites).contains(string(R.string.oneOfMyFavouriteSpotAvailable));
    }


}
