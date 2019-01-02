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
import java.util.Collection;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import modelo.SpotsManager;
import modelo.UsersManager;
import steps.US19FeatureSteps;

@RunWith(Parameterized.class)
public class US19CancelFeatureTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(DashboardAuthActivity.class);

    public US19CancelFeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Test
    public void test() {
        start(new US19FeatureSteps());
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS19Cancel.feature").scenarios();
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {

        SpotsManager.INSTANCE.addSpotToDatabase("TestSpot", "A", "39.735008,-8.820593", 1, 0);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("spots7@email.pt", "12345678");

        while(!registerTask.isComplete())
            Thread.sleep(1);


        if(registerTask.isSuccessful()){
            UsersManager.INSTANCE.addUserThatIsParked("Spots","spots7@email.pt", "TestSpot", null);
        }
        else{
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("spots7@email.pt", "12345678");

            while(!loginTask.isComplete())
                Thread.sleep(1);

            UsersManager.INSTANCE.addUserThatIsParked("Spots","spots7@email.pt", "TestSpot", null);
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
            //Se não fazer login - não deve acontecer em principio ele esta logado sempre - e eliminar
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("spots3@email.pt", "12345678");

            //todo - não é a melhor solução mas em termos de performance é melhor que sleep
            while(!loginTask.isComplete())
                Thread.sleep(1);

            if(loginTask.isSuccessful()){
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                currentUser.delete();

                FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();

            }
        }

        SpotsManager.INSTANCE.removeSpotFromDatabase("TestSpot");
    }



}
