package features;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import groupf.taes.ipleiria.spots.DashboardActivity;
import groupf.taes.ipleiria.spots.LoginActivity;
import modelo.UsersManager;
import steps.US2FeatureSteps;

@RunWith(Parameterized.class)
public class US2FeatureTest extends GreenCoffeeTest {
    private static Object lock = new Object();
    private static boolean ready = false;

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(DashboardActivity.class);

    public US2FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS2.feature").scenarios();
    }

    @BeforeClass
    public static synchronized void setUpOnlyOnce() throws Exception {
        IdlingRegistry.getInstance().register(LoginActivity.getIdlingResource());


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


            /*FirebaseAuth.getInstance().createUserWithEmailAndPassword("test@test.test", "12345678")
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            synchronized (lock) {
                                if (task.isSuccessful()) {
                                    if (FirebaseAuth.getInstance().getCurrentUser() != null)
                                        FirebaseAuth.getInstance().signOut();
                                } else {
                                    Log.println(1, "Exception US2 - beforeClass", task.getException().getMessage());
                                }
                                ready = true;
                                lock.notify();
                            }
                        }
                    });*/


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
        /*synchronized (lock) {
            while (!ready) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            start(new US2FeatureSteps());
        }*/
        start(new US2FeatureSteps());
    }

    //Faz logout antes de scenario
    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            FirebaseAuth.getInstance().signOut();

    }
}
