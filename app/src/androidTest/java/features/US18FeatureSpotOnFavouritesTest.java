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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.UsersManager;
import steps.US18FeatureSpotOnFavouritesSteps;

@RunWith(Parameterized.class)
public class US18FeatureSpotOnFavouritesTest extends GreenCoffeeTest {
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule .grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(DashboardAuthActivity.class);

    public US18FeatureSpotOnFavouritesTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Test
    public void test() {
        start(new US18FeatureSpotOnFavouritesSteps());
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS18SpotOnFavourites.feature").scenarios();
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {

        SpotsManager.INSTANCE.addSpotToDatabase("TestSpot", "A", "39.735008,-8.820593", 1, 0);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();


        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("spots7@email.pt", "12345678");

        while(!registerTask.isComplete())
            Thread.sleep(1);

        List<Spot> spots=new ArrayList<>();
        spots.add(new Spot("TestSpot", "A", "39.735008,-8.820593", 1, 0));

        if(registerTask.isSuccessful()){
           UsersManager.INSTANCE. addUserThatIsParked("Spots","spots7@email.pt", "TestSpot", spots);
        }
        else{
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("spots7@email.pt", "12345678");

            while(!loginTask.isComplete())
                Thread.sleep(1);


            UsersManager.INSTANCE.addUserThatIsParked("Spots","spots7@email.pt", "TestSpot", spots);
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
