package features;

import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import groupf.taes.ipleiria.spots.DashboardActivity;
import modelo.UsersManager;
import steps.US5FeatureWithoutPreferencesSteps;

import static android.os.SystemClock.sleep;

@RunWith(Parameterized.class)
public class US5FeatureWithoutPreferencesTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(DashboardActivity.class);

    public US5FeatureWithoutPreferencesTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS5WithoutPreferences.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US5FeatureWithoutPreferencesSteps());
    }

    //Assegurar que existe o utilizador de teste na BD Auth e na BD de Users

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }
    }

    //Apagar esse user de teste da BD auth e da BD de Users
}
