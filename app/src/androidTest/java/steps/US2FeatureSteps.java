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

public class US2FeatureSteps extends GreenCoffeeSteps {

    @Given("^I am an anonymous user$")
    public void i_am_an_anonymous_user() {
        //Garante que utilizador é anónimo (como ele fica logado sempre temos de fazer o signOut no teste)
        Assert.assertNull(FirebaseAuth.getInstance().getCurrentUser());
    }

    @When("^I am in the dashboard screen$")
    public void i_am_in_the_dashboard_screen() {
        onViewWithId(R.id.btnLogin).isDisplayed().check(matches(withText(R.string.signUpButton)));
    }

    @When("^I press the Login button in dashboard$")
    public void i_press_the_Login_button_in_dashboard() {
        closeKeyboard();
        onViewWithId(R.id.btnLogin).click();
    }

    @When("^I see the login screen$")
    public void i_see_the_login_screen() {
        onViewWithId(R.id.buttonLogin).isDisplayed().check(matches(withText(R.string.btnLogin)));
        onViewWithId(R.id.buttonRegister).isDisplayed().check(matches(withText(R.string.btnRegister)));
        onViewWithText("Email").isDisplayed();
        onViewWithText("Password").isDisplayed();
    }

    @Then("^I see all the fields displayed and empty$")
    public void i_see_all_the_fields_displayed_and_empty() {
        onViewWithId(R.id.editTextEmail).isDisplayed().isEmpty();
        onViewWithId(R.id.editTextPassword).isDisplayed().isEmpty();
    }

    @Given("^I type my email on email field \"([^\"]*)\"$")
    public void i_type_my_email_on_email_field(String arg1) {
        //Escreve email e conforma o que escreveu
        closeKeyboard();
        onViewWithId(R.id.editTextEmail).type(arg1);
        onViewWithId(R.id.editTextEmail).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @Given("^I type my password on password field \"([^\"]*)\"$")
    public void i_type_my_password_on_password_field(String arg1) {
        closeKeyboard();
        onViewWithId(R.id.editTextPassword).type(arg1);
        onViewWithId(R.id.editTextPassword).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @Given("^I press the Login button in login activity$")
    public void i_press_the_Login_button_in_login_activity() {
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @Then("^I see the authenticated dashboard page$")
    public void i_see_the_authenticated_dashboard_page() {
        //todo tratar caso do sleep para sincronização de threads
        sleep(5000);
        onViewWithId(R.id.btnProfile).isDisplayed();
    }

    @Given("^I introduce an invalid email format \"([^\"]*)\"$")
    public void i_introduce_an_invalid_email_format(String arg1) {
        onViewWithId(R.id.editTextEmail).type(arg1);
        onViewWithId(R.id.editTextEmail).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @Then("^I see an error message in email field saying \"([^\"]*)\"$")
    public void i_see_an_error_message_in_email_field_saying(String arg1) {
        onViewWithId(R.id.editTextEmail).hasErrorText(arg1);
    }

    @Given("^I introduce an email that does not exist on database \"([^\"]*)\"$")
    public void i_introduce_an_email_that_does_not_exist_on_database(String arg1) {
        onViewWithId(R.id.editTextEmail).type(arg1);
        onViewWithId(R.id.editTextEmail).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @Then("^I see an error message saying \"([^\"]*)\"$")
    public void i_see_an_error_message_saying(String arg1) {
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(arg1)).inRoot(isDialog()).perform(click());
    }

    @Then("^I see an error message in password field saying \"([^\"]*)\"$")
    public void i_see_an_error_message_in_password_field_saying(String arg1) {
        onViewWithId(R.id.editTextPassword).hasErrorText(arg1);
    }
}
