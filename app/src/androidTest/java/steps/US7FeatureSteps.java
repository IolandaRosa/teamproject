package steps;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import junit.framework.Assert;

import groupf.taes.ipleiria.spots.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class US7FeatureSteps extends GreenCoffeeSteps {

    @Given("^I am an authenticate user$")
    public void i_am_an_authenticate_user() {
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I am in my profile screen$")
    public void i_am_in_my_profile_screen() {
        onViewWithId(R.id.btnMyPreferences).isDisplayed().check(matches(withText(R.string.btnMyPreferences)));
        onViewWithId(R.id.btnFavouriteSpots).isDisplayed().check(matches(withText(R.string.btnFavouriteSpots)));
        onViewWithId(R.id.btnUpdateMyProfile).isDisplayed().check(matches(withText(R.string.btnUpdateMyProfile)));
        onViewWithText(R.string.name).isDisplayed();
        onViewWithId(R.id.txtViewName).isDisplayed().check(matches(withText("Maria Leopoldina")));
        onViewWithText(R.string.email).isDisplayed();
        onViewWithId(R.id.txtViewEmail).isDisplayed().check(matches(withText("maria_leopoldina@email.pt")));
        onViewWithText(R.string.findMeASpotPreference).isDisplayed();
        onViewWithId(R.id.textViewPreference).isDisplayed().check(matches(withText("None")));
    }

    @When("^I click on the \"([^\"]*)\" button$")
    public void i_click_on_the_button(String arg1) {
        onViewWithId(R.id.btnUpdateMyProfile).click();
    }

    @Then("^I am on update my profile screen$")
    public void i_am_on_update_my_profile_screen() {
        onViewWithId(R.id.btnSave).isDisplayed().check(matches(withText(R.string.save)));
        onViewWithId(R.id.btnCancel).isDisplayed().check(matches(withText(R.string.cancel)));
    }

    @Then("^I see all the fields displaying my current information$")
    public void i_see_all_the_fields_displaying_my_current_information() {
        onViewWithText(R.string.name).isDisplayed();
        onViewWithId(R.id.editTextName).isDisplayed().isNotEmpty();
        onViewWithText(R.string.email).isDisplayed();
        onViewWithId(R.id.editTextEmail).isDisplayed().isNotEmpty();
        onViewWithText(R.string.findMeASpotPreference).isDisplayed();
        onViewWithId(R.id.spinnerFindPreference).isDisplayed();
    }

    @When("^I see the spinner preferences dispalyed$")
    public void i_see_the_spinner_preferences_dispalyed() {
        onViewWithId(R.id.spinnerFindPreference).isDisplayed();
    }

    @When("^I click to open the spinner$")
    public void i_click_to_open_the_spinner() {
        onViewWithId(R.id.spinnerFindPreference).click();
    }

    @Then("^I see the option None displayed$")
    public void i_see_the_option_None_displayed() {
        onData(is("None")).perform(click());
        onViewWithId(R.id.spinnerFindPreference).check(matches(withSpinnerText(containsString("None"))));
    }

    @Then("^I see the option Closer To My Location displayed$")
    public void i_see_the_option_Closer_To_My_Location_displayed() {
        onData(is("Closer To My Location")).perform(click());
        onViewWithId(R.id.spinnerFindPreference).check(matches(withSpinnerText(containsString("Closer To My Location"))));
    }

    @Then("^I see the option Best Rated Place displayed$")
    public void i_see_the_option_Best_Rated_Place_displayed() {
        onData(is("Best Rated Place")).perform(click());
        onViewWithId(R.id.spinnerFindPreference).check(matches(withSpinnerText(containsString("Best Rated Place"))));
    }

    @Then("^I see the option My Favourite Spots displayed$")
    public void i_see_the_option_My_Favourite_Spots_displayed() {
        onData(is("My Favourite Spots")).perform(click());
        onViewWithId(R.id.spinnerFindPreference).check(matches(withSpinnerText(containsString("My Favourite Spots"))));
        onViewWithId(R.id.spinnerFindPreference).click();
        onData(is("None")).perform(click());
    }

    @When("^I see the name is empty$")
    public void i_see_the_name_is_empty() {
        onViewWithId(R.id.editTextName).perform(replaceText(" "));
    }

    @When("^I see the email is empty$")
    public void i_see_the_email_is_empty() {
        onViewWithId(R.id.editTextEmail).perform(replaceText(" "));
    }

    @When("^I press the \"([^\"]*)\" button$")
    public void i_press_the_button(String arg1) {
        onViewWithId(R.id.btnSave).isDisplayed().check(matches(withText(arg1))).click();
    }

    @Then("^I see an error message displayed saying \"([^\"]*)\"$")
    public void i_see_an_error_message_displayed_saying(String arg1) {
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
    }

    @When("^I see the email not empty$")
    public void i_see_the_email_not_empty() {

    }

    @Then("^I see an error message on the name field saying \"([^\"]*)\"$")
    public void i_see_an_error_message_on_the_name_field_saying(String arg1) {

    }

    @When("^I type \"([^\"]*)\" on the name field$")
    public void i_type_on_the_name_field(String arg1) {

    }

    @When("^I see the name not empty$")
    public void i_see_the_name_not_empty() {

    }

    @Then("^I see an error message on the email field saying \"([^\"]*)\"$")
    public void i_see_an_error_message_on_the_email_field_saying(String arg1) {

    }

    @When("^I type \"([^\"]*)\" on email$")
    public void i_type_on_email(String arg1) {

    }

    @When("^I type my name \"([^\"]*)\" on name field$")
    public void i_type_my_name_on_name_field(String arg1) {

    }

    @Then("^I see the profile activity with my new name displayed$")
    public void i_see_the_profile_activity_with_my_new_name_displayed() {

    }

    @When("^I type new email \"([^\"]*)\" on email field$")
    public void i_type_new_email_on_email_field(String arg1) {

    }

    @When("^I see the password confirmation displayed$")
    public void i_see_the_password_confirmation_displayed() {

    }

    @When("^I press the Ok button$")
    public void i_press_the_Ok_button() {

    }

    @When("^I type \"([^\"]*)\" on password field$")
    public void i_type_on_password_field(String arg1) {

    }

    @Then("^I see the profile activity with my new email dispalyed$")
    public void i_see_the_profile_activity_with_my_new_email_dispalyed() {

    }

    @Then("^I confirm the authentication email is now \"([^\"]*)\"$")
    public void i_confirm_the_authentication_email_is_now(String arg1) {

    }

    @When("^I click on the spinner$")
    public void i_click_on_the_spinner() {

    }

    @When("^I choose the option \"([^\"]*)\"$")
    public void i_choose_the_option(String arg1) {

    }

    @Then("^I see the profile activity with my new preference dispalyed$")
    public void i_see_the_profile_activity_with_my_new_preference_dispalyed() {

    }

    @Then("^I see the profile activity with my new information dispalyed$")
    public void i_see_the_profile_activity_with_my_new_information_dispalyed() {

    }

    @Then("^I press the Cancel button$")
    public void i_press_the_Cancel_button() {

    }

    @Then("^I see the profile activity displayed$")
    public void i_see_the_profile_activity_displayed() {

    }
}
