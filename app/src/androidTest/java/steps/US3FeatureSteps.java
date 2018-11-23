package steps;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import junit.framework.Assert;

import groupf.taes.ipleiria.spots.R;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class US3FeatureSteps extends GreenCoffeeSteps {


    @When("^I see the error message \"([^\"]*)\" on the confirmation password field$")
    public void i_see_the_error_message_on_the_confirmation_password_field(String arg1) {
        onViewWithId(R.id.editTextConfirmationPassword).hasErrorText(arg1);
    }

    @Given("^I am an anonymous user$")
    public void i_am_an_anonymous_user() {
        Assert.assertNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I am in the login screen$")
    public void i_am_in_the_login_screen() {
        onViewWithId(R.id.buttonLogin).isDisplayed().check(matches(withText(R.string.btnLogin)));
        onViewWithId(R.id.buttonRegister).isDisplayed().check(matches(withText(R.string.btnRegister)));
        onViewWithText("Email").isDisplayed();
        onViewWithText("Password").isDisplayed();
    }

    @When("^I press the register button in the login screen$")
    public void i_press_the_register_button_in_the_login_screen() {
        closeKeyboard();
        onViewWithId(R.id.buttonRegister).click();
    }

    @When("^I see the register screen$")
    public void i_see_the_register_screen() {
        onViewWithId(R.id.btnRegister).isDisplayed().check(matches(withText(R.string.btnRegister)));
        onViewWithId(R.id.btnAlreadyAuth).isDisplayed().check(matches(withText(R.string.alreadyAuth)));
        onViewWithText("Email").isDisplayed();
        onViewWithText("Name").isDisplayed();
        onViewWithText("Password").isDisplayed();
    }

    @Then("^I see all the fields displayed and empty$")
    public void i_see_all_the_fields_displayed_and_empty() {
        onViewWithId(R.id.editTextEmail).isDisplayed().isEmpty();
        onViewWithId(R.id.editTextPassword).isDisplayed().isEmpty();
        onViewWithId(R.id.editTextConfirmationPassword).isDisplayed().isEmpty();
        onViewWithId(R.id.editTextName).isDisplayed().isEmpty();
    }

    @Given("^I type my name \"([^\"]*)\" on name field$")
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

    @When("^I am registered with success$")
    public void i_am_registered_with_success() {
        //Regista e faz login logo tem sucesso
        //todo tratar caso do sleep para sincronização de threads
        sleep(5000);
        Assert.assertNotNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^A new user whith email \"([^\"]*)\" is added on database$")
    public void a_new_user_whith_name_and_email_is_added_on_database(String arg1) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Assert.assertEquals(arg1,email);
    }

    @Then("^I see the autheticated user dashboard$")
    public void i_see_the_autheticated_user_dashboard() {
        onViewWithId(R.id.mapFragment).isDisplayed();
        onViewWithId(R.id.spinner).isDisplayed();
        onViewWithId(R.id.drawer_layout).isDisplayed();
    }

    @When("^I see the error message on screen \"([^\"]*)\"$")
    public void i_see_a_error_message_on_screen(String arg1) {
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
    }

    @Then("^The application assert that no user was authenticated and added on database$")
    public void the_application_assert_that_no_user_was_authenticated_and_added_on_database() {
        Assert.assertNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Given("^I type my name with numbers \"([^\"]*)\" on name field$")
    public void i_type_my_name_with_numbers_on_name_field(String arg1) {
        onViewWithId(R.id.editTextName).type(arg1);
        onViewWithId(R.id.editTextName).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I see the error message \"([^\"]*)\" on the name field$")
    public void i_see_the_error_message_on_the_name_field(String arg1) {
        onViewWithId(R.id.editTextName).hasErrorText(arg1);
    }

    @When("^I see the error message \"([^\"]*)\" on the email field$")
    public void i_see_the_error_message_on_the_email_field(String arg1) {
        onViewWithId(R.id.editTextEmail).hasErrorText(arg1);
    }

    @When("^I see the error message \"([^\"]*)\" on the password field$")
    public void i_see_the_error_message_on_the_password_field(String arg1) {
        onViewWithId(R.id.editTextPassword).hasErrorText(arg1);
    }



}
