package features;

import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import modelo.FindPreference;
import modelo.Spot;
import modelo.UsersManager;
import steps.US12FeatureAllSpotsOccupiedSteps;

@RunWith(Parameterized.class)
public class US12FeatureAllSpotsOccupiedTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(DashboardAuthActivity.class);

    public US12FeatureAllSpotsOccupiedTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS12AllSpotsOccupied.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US12FeatureAllSpotsOccupiedSteps());
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("spots3@email.pt", "12345678");

        while(!registerTask.isComplete())
            Thread.sleep(1);

        List<Spot> spots=new ArrayList<>();
        spots.add(new Spot("A-1","D","1,2",1,4,0));
        spots.add(new Spot("A-2","D","-1,5",1,0,0));
        spots.add(new Spot("A-4","A","-1,5",1,5,0));

        if(registerTask.isSuccessful()){
            UsersManager.INSTANCE.addUserWithSpotsToDatabase("Spots","spots3@email.pt", spots);
            UsersManager.INSTANCE.addFindPreferenceToAUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),FindPreference.FAVOURITE_SPOTS);
        }
        else{
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("spots3@email.pt", "12345678");

            while(!loginTask.isComplete())
                Thread.sleep(1);

            UsersManager.INSTANCE.addUserWithSpotsToDatabase("Spots","spots3@email.pt", spots);
            UsersManager.INSTANCE.addFindPreferenceToAUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),FindPreference.FAVOURITE_SPOTS);
        }
    }


    @AfterClass
    public static void tearDownOnlyOnce() throws Throwable {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();
            currentUser.delete();

            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
        }else{

            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("spots3@email.pt", "12345678");

            while(!loginTask.isComplete())
                Thread.sleep(1);

            if(loginTask.isSuccessful()){
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                currentUser.delete();

                FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
            }
        }
    }

}
