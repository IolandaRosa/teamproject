package steps;

import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import groupf.taes.ipleiria.spots.R;
import helpers.DrawerHelper;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US8FeatureSteps extends GreenCoffeeSteps {


    @Given("^I am an authenticated user$")
    public void i_am_an_authenticated_user() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
        onViewWithId(R.id.drawer_layout).isDisplayed();
    }

    @When("^I select the menu option \"([^\"]*)\" the dashboard autheticated screen$")
    public void i_select_the_menu_option_the_dashboard_autheticated_screen(String arg1) {
        onView(withId(R.id.drawer_layout)).perform(DrawerHelper.actionOpenDrawer());
        onView(withText(arg1)).perform(click());
    }

    @When("^I see the update password screen displayed$")
    public void i_see_the_update_password_screen_displayed() {
        onViewWithText(string(R.string.current_password)).isDisplayed();
        onViewWithText(string(R.string.new_password)).isDisplayed();
        onViewWithText(string(R.string.new_password_confirmation)).isDisplayed();
        onViewWithId(R.id.btnSave).isDisplayed().check(matches(withText(string(R.string.save)))).isDisplayed();
        onViewWithId(R.id.btnCancel).isDisplayed().check(matches(withText(string(R.string.cancel)))).isDisplayed();
    }

    @Then("^I see al the fields empty$")
    public void i_see_al_the_fields_empty() {
        onViewWithId(R.id.editOldPassword).isDisplayed().isEmpty();
        onViewWithId(R.id.editNewPassword).isDisplayed().isEmpty();
        onViewWithId(R.id.editNewPasswordConfirmation).isDisplayed().isEmpty();
    }

    @Then("^I see an error message saying \"([^\"]*)\"$")
    public void i_see_an_error_message_saying(String arg1) {
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(arg1)).inRoot(isDialog()).perform(click());
    }

    @When("^I type my confirmation password \"([^\"]*)\" on new confirmation password field$")
    public void i_type_my_confirmation_password_on_new_confirmation_password_field(String arg1) {
        onViewWithId(R.id.editNewPasswordConfirmation).type(arg1);
        onViewWithId(R.id.editNewPasswordConfirmation).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I click the Save button$")
    public void i_click_the_Save_button() {
        closeKeyboard();
        onViewWithId(R.id.btnSave).click();
    }

    @Then("^I see an error message on the current password input field saying \"([^\"]*)\"$")
    public void i_see_an_error_message_on_the_current_password_input_field_saying(String arg1) {
        onViewWithId(R.id.editOldPassword).hasErrorText(arg1);
    }

    @Given("^I type my current password \"([^\"]*)\" on current password input field$")
    public void i_type_my_current_password_on_current_password_input_field(String arg1) {
        onViewWithId(R.id.editOldPassword).type(arg1);
        onViewWithId(R.id.editOldPassword).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I type my new password \"([^\"]*)\" on new password input field$")
    public void i_type_my_new_password_on_new_password_input_field(String arg1) {
        onViewWithId(R.id.editNewPassword).type(arg1);
        onViewWithId(R.id.editNewPassword).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @Then("^I see an error message on the new password input field saying \"([^\"]*)\"$")
    public void i_see_an_error_message_on_the_new_password_input_field_saying(String arg1) {
        onViewWithId(R.id.editNewPassword).hasErrorText(arg1);
    }

    @Then("^I see an error message on the new password confirmation input field saying \"([^\"]*)\"$")
    public void i_see_an_error_message_on_the_new_password_confirmation_input_field_saying(String arg1) {
        onViewWithId(R.id.editNewPasswordConfirmation).hasErrorText(arg1);
    }

    @Then("^I was redirected to the dashboard authenticated screen$")
    public void i_was_redirected_to_the_dashboard_authenticated_screen() {
        sleep(3000);
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
        onViewWithId(R.id.drawer_layout).isDisplayed();
    }

    @Given("^I click the Cancel button$")
    public void i_click_the_Cancel_button() {
        closeKeyboard();
        onViewWithId(R.id.btnCancel).click();
    }
}
