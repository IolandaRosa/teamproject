package features;

import android.net.wifi.WifiManager;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import groupf.taes.ipleiria.spots.LoginActivity;
import steps.US2FeatureWithoutConnectionSteps;

import static android.content.Context.WIFI_SERVICE;

@RunWith(Parameterized.class)
public class US2FeatureTestWithoutConnection extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(LoginActivity.class);

    public US2FeatureTestWithoutConnection(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS2WithoutConnection.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US2FeatureWithoutConnectionSteps());
    }


    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();
    }

    @Before
    public void setUp() throws Exception {
        WifiManager wm = (WifiManager) activityTestRule.getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        wm.setWifiEnabled(false);
    }

    @After
    public void tearDown() throws Exception {
        WifiManager wm = (WifiManager) activityTestRule.getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        wm.setWifiEnabled(true);
    }
}
