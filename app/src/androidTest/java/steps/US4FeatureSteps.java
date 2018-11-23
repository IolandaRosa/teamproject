package steps;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
;
import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import org.junit.Assert;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;
import modelo.SpotsManager;


import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class US4FeatureSteps extends GreenCoffeeSteps {
    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I enter the application$")
    public void i_enter_the_application() {
        onViewWithText(string(R.string.app_name)).isDisplayed();
    }

    @When("^I see the authenticated user dashboard$")
    public void i_see_the_authenticated_user_dashboard() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
    }

    @When("^I see the free spots markers on the map$")
    public void i_see_the_free_spots_markers_on_the_map() {
        Assert.assertEquals(DashboardAuthActivity.getMarkers().size(), SpotsManager.getINSTANCE().getFreeSpotsParkA());
    }

    @When("^I see the information of total free spots, total of ocupied spots and the date of update$")
    public void i_see_the_information_of_total_free_spots_total_of_ocupied_spots_and_the_date_of_update() {
        onViewWithId(R.id.txtNumberFreeSpots).isDisplayed();
        onViewWithId(R.id.txtNumberFreeSpots).contains(String.valueOf(SpotsManager.getINSTANCE().getFreeSpotsParkA()));

        onViewWithId(R.id.txtNumberOcuppiedSpots).isDisplayed();
        onViewWithId(R.id.txtNumberOcuppiedSpots).contains(String.valueOf(SpotsManager.getINSTANCE().getOcuppiedSpotsParkA()));

        onViewWithId(R.id.lastInfoDate).isDisplayed();
        onViewWithId(R.id.lastInfoDate).contains(String.valueOf(SpotsManager.getINSTANCE().getDateOfData()));
    }

    @When("^I see the hamburger button for the menu $")
    public void i_see_the_hamburger_button_for_the_menu() {
        onViewWithId(R.id.drawer_layout).isDisplayed();
    }

    @When("^I click on the hambuguer button$")
    public void i_click_on_the_hambuguer_button() {
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
    }

    @Then("^I see the options: Profile, Find me a spot, My favourite Spots, My Spot, Statistics, Change My Password, Logout, Dashboard$")
    public void i_see_the_options_Profile_Find_me_a_spot_My_favourite_Spots_My_Spot_Statistics_Change_My_Password_Logout_Dashboard() {
        String[] options = InstrumentationRegistry.getTargetContext().getResources().getStringArray(R.array.dashboradIems);
        for (String s : options) {
            onViewWithText(s).isDisplayed();
        }
    }

    @When("^I choose other park on the spinner$")
    public void i_choose_other_park_on_the_spinner() {
        onViewWithId(R.id.spinner).click();
        String[] mapsOptions = InstrumentationRegistry.getTargetContext().getResources().getStringArray(R.array.maps);
        onViewWithText(mapsOptions[1]).click();
    }

    @Then("^I see that park on the map$")
    public void i_see_that_park_on_the_map() {


    }

    @When("^I see the free spots markers on the map of the other park$")
    public void i_see_the_free_spots_markers_on_the_map_of_the_other_park() {
        Assert.assertEquals(DashboardAuthActivity.getMarkers().size(), SpotsManager.getINSTANCE().getFreeSpotsParkD());
    }


    @Then("^the information of total free spots and total of ocupied spots for that park$")
    public void the_information_of_total_free_spots_and_total_of_ocupied_spots_for_that_park() {
        onViewWithId(R.id.txtNumberFreeSpots).isDisplayed();
        onViewWithId(R.id.txtNumberFreeSpots).contains(String.valueOf(SpotsManager.getINSTANCE().getFreeSpotsParkD()));

        onViewWithId(R.id.txtNumberOcuppiedSpots).isDisplayed();
        onViewWithId(R.id.txtNumberOcuppiedSpots).contains(String.valueOf(SpotsManager.getINSTANCE().getOcuppiedSpotsParkD()));

        onViewWithId(R.id.lastInfoDate).isDisplayed();
        onViewWithId(R.id.lastInfoDate).contains(String.valueOf(SpotsManager.getINSTANCE().getDateOfData()));
    }

}
