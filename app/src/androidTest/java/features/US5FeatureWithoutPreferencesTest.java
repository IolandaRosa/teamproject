package features;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import groupf.taes.ipleiria.spots.ProfileActivity;
import modelo.UsersManager;
import steps.US5FeatureWithoutPreferencesSteps;

@RunWith(Parameterized.class)
public class US5FeatureWithoutPreferencesTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(DashboardAuthActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule .grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    public US5FeatureWithoutPreferencesTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS5WithoutPreferences.feature").scenarios();
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {
        IdlingRegistry.getInstance().register(ProfileActivity.getIdlingResource());

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            FirebaseAuth.getInstance().signOut();

        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("maria@email.pt", "12345678");

        while (!registerTask.isComplete())
            Thread.sleep(1);

        if (registerTask.isSuccessful()) {
            UsersManager.INSTANCE.addUserToDatabase("Maria Pt", "maria@email.pt");
        } else {
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("maria@email.pt", "12345678");

            while (!loginTask.isComplete())
                Thread.sleep(1);

            if (loginTask.isSuccessful()) {
                DatabaseReference users = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                if (users == null) {
                    UsersManager.INSTANCE.addUserToDatabase("Maria Pt", "maria@email.pt");
                }
            }
        }
    }

    @AfterClass
    public static void tearDownOnlyOnce() throws Throwable {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();
            currentUser.delete();

            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
        } else {
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("maria@email.pt", "12345678");

            while (!loginTask.isComplete())
                Thread.sleep(1);

            if (loginTask.isSuccessful()) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                currentUser.delete();

                FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
            }
        }
    }

    @Test
    public synchronized void test() {
        start(new US5FeatureWithoutPreferencesSteps());
    }
}
