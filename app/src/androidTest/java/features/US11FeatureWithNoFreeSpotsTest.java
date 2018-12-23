package features;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

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
import java.util.Collection;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import modelo.FindPreference;
import modelo.UsersManager;
import steps.US11FeatureWithNoFreeSpotsSteps;

@RunWith(Parameterized.class)
public class US11FeatureWithNoFreeSpotsTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(DashboardAuthActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule .grant(android.Manifest.permission.ACCESS_FINE_LOCATION);



    public US11FeatureWithNoFreeSpotsTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS11WithNoFreeSpots.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US11FeatureWithNoFreeSpotsSteps());
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();


        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("spots3@email.pt", "12345678");


        while(!registerTask.isComplete())
            Thread.sleep(1);


        if(registerTask.isSuccessful()){

            UsersManager.INSTANCE.addUserToDatabase("Spots","spots3@email.pt");
            UsersManager.INSTANCE.addFindPreferenceToAUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),FindPreference.CLOSER_LOCATION);
        }
        else{
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("spots3@email.pt", "12345678");

            while(!loginTask.isComplete())
                Thread.sleep(1);


            UsersManager.INSTANCE.addUserToDatabase("Spots","spots3@email.pt");
            UsersManager.INSTANCE.addFindPreferenceToAUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),FindPreference.CLOSER_LOCATION);
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
