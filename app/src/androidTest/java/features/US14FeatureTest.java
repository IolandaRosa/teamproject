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
import modelo.Spot;
import modelo.UsersManager;
import steps.US13FeatureWithFavouritesListSteps;
import steps.US14FeatureSteps;

@RunWith(Parameterized.class)
public class US14FeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(DashboardAuthActivity.class);

    public US14FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS14.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US14FeatureSteps());
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        //regista o utilizador
        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("spots2@email.pt", "12345678");

        //todo - não é a melhor solução mas em termos de performance é melhor que sleep
        while(!registerTask.isComplete())
            Thread.sleep(1);

        List<Spot> spots=new ArrayList<>();
        spots.add(new Spot("A-1","D","1,2",0,4));
        spots.add(new Spot("A-2","D","-1,5",1,0));

        if(registerTask.isSuccessful()){
            //Coloca utilizador na BD sem spots
            UsersManager.INSTANCE.addUserWithSpotsToDatabase("Spots","spots2@email.pt", spots);
        }
        else{
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("spots2@email.pt", "12345678");

            //todo - não é a melhor solução mas em termos de performance é melhor que sleep
            while(!loginTask.isComplete())
                Thread.sleep(1);

            //Coloca utilizador na BD sem spots
            UsersManager.INSTANCE.addUserWithSpotsToDatabase("Spots","spots2@email.pt", spots);
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
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("spots2@email.pt", "12345678");

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
    }
}