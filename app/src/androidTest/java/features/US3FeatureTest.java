package features;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import groupf.taes.ipleiria.spots.LoginActivity;
import groupf.taes.ipleiria.spots.RegisterActivity;
import modelo.UsersManager;
import steps.US3FeatureSteps;

@RunWith(Parameterized.class)
public class US3FeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(LoginActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule .grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    public US3FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS3.feature").scenarios();
    }

    @BeforeClass
    public static synchronized void setUpOnlyOnce() throws Exception {
        IdlingRegistry.getInstance().register(RegisterActivity.getIdlingResource());

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            FirebaseAuth.getInstance().signOut();

        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("test@test.test", "12345678");

        while (!registerTask.isComplete())
            Thread.sleep(1);

        if (registerTask.isSuccessful()) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                FirebaseAuth.getInstance().signOut();
        } else {

            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("test@test.test", "12345678");


            while (!loginTask.isComplete())
                Thread.sleep(1);
        }
    }

    @AfterClass
    public static void tearDownOnlyOnce() throws Exception {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }

        Task<AuthResult> authResultTask = FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.test", "12345678");

        while (!authResultTask.isComplete())
            Thread.sleep(1);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().delete();
        }
    }

    @Test
    public synchronized void test() {
        start(new US3FeatureSteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            FirebaseAuth.getInstance().signOut();
    }

    @After
    public void testStart() throws Throwable {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();
            currentUser.delete();

            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
        }
    }
}
