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

public class US2FeatureWithoutConnectionSteps extends GreenCoffeeSteps {

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

    @When("^I see all the fields displayed and empty$")
    public void i_see_all_the_fields_displayed_and_empty() {
        onViewWithId(R.id.editTextEmail).isDisplayed().isEmpty();
        onViewWithId(R.id.editTextPassword).isDisplayed().isEmpty();
    }

    @When("^I type my email on email field \"([^\"]*)\"$")
    public void i_type_my_email_on_email_field(String arg1) {
        onViewWithId(R.id.editTextEmail).type(arg1);
        onViewWithId(R.id.editTextEmail).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I type my password on password field \"([^\"]*)\"$")
    public void i_type_my_password_on_password_field(String arg1) {
        onViewWithId(R.id.editTextPassword).type(arg1);
        onViewWithId(R.id.editTextPassword).isDisplayed().isNotEmpty().check(matches(withText(arg1)));
    }

    @When("^I press the Login button in login activity$")
    public void i_press_the_Login_button_in_login_activity() {
        closeKeyboard();
        onViewWithId(R.id.buttonLogin).click();
    }

    @Then("^I see an error message saying \"([^\"]*)\"$")
    public void i_see_an_error_message_saying(String arg1) {
        onView(withText(arg1)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(arg1)).inRoot(isDialog()).perform(click());
    }
}
