package features;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import groupf.taes.ipleiria.spots.DashboardActivity;
import steps.US2FeatureSteps;

@RunWith(Parameterized.class)
public class US2FeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(DashboardActivity.class);

    public US2FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS2.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US2FeatureSteps());
    }

}
