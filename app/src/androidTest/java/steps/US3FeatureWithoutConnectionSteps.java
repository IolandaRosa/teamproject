package steps;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import junit.framework.Assert;

import groupf.taes.ipleiria.spots.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US3FeatureWithoutConnectionSteps extends GreenCoffeeSteps {

    @Given("^I am an anonymous user$")
    public void i_am_an_anonymous_user() {
        Assert.assertNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I am in the register screen$")
    public void i_am_in_the_register_screen() {
        onViewWithId(R.id.btnRegister).isDisplayed().check(matches(withText(R.string.btnRegister)));
        onViewWithId(R.id.btnAlreadyAuth).isDisplayed().check(matches(withText(R.string.alreadyAuth)));
        onViewWithText("Email").isDisplayed();
        onViewWithText("Name").isDisplayed();
        onViewWithText("Password").isDisplayed();
    }

    @When("^I see all the fields displayed and empty$")
    public void i_see_all_the_fields_displayed_and_empty() {
        onViewWithId(R.id.editTextEmail).isDisplayed().isEmpty();
        onViewWithId(R.id.editTextPassword).isDisplayed().isEmpty();
        onViewWithId(R.id.editTextConfirmationPassword).isDisplayed().isEmpty();
        onViewWithId(R.id.editTextName).isDisplayed().isEmpty();
    }

    @When("^I type my name \"([^\"]*)\" on name field$")
    public void i_type_my_name_on_name_field(String arg1) {
        onViewWithId(R.id.editTextName).type(arg1);
        onViewWithId(R.id.editTextName).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I type my email \"([^\"]*)\" on email field$")
    public void i_type_my_email_on_email_field(String arg1) {
        onViewWithId(R.id.editTextEmail).type(arg1);
        onViewWithId(R.id.editTextEmail).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I type my password \"([^\"]*)\" on password field$")
    public void i_type_my_password_on_password_field(String arg1) {
        onViewWithId(R.id.editTextPassword).type(arg1);
        onViewWithId(R.id.editTextPassword).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I type my confirmation password \"([^\"]*)\" on confirmation password field$")
    public void i_type_my_confirmation_password_on_confirmation_password_field(String arg1) {
        closeKeyboard();
        onViewWithId(R.id.editTextConfirmationPassword).type(arg1);
        onViewWithId(R.id.editTextConfirmationPassword).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I click the register button in the register screen$")
    public void i_click_the_register_button_in_the_register_screen() {
        closeKeyboard();
        onViewWithId(R.id.btnRegister).click();
    }

    @Then("^I see an error message saying \"([^\"]*)\"$")
    public void i_see_an_error_message_saying(String arg1) {
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

    }



}
