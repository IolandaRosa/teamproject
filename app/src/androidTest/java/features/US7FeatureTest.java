package features;

import android.support.test.rule.ActivityTestRule;

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

import groupf.taes.ipleiria.spots.ProfileActivity;
import modelo.UsersManager;
import steps.US7FeatureSteps;

@RunWith(Parameterized.class)
public class US7FeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(ProfileActivity.class);

    public US7FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS7.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US7FeatureSteps());
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("maria_leopoldina@email.pt", "12345678");

        //todo - sincronização threads
        while(!registerTask.isComplete())
            Thread.sleep(1);

        if(registerTask.isSuccessful()){
            UsersManager.INSTANCE.addUserToDatabase("Maria Leopoldina","maria_leopoldina@email.pt"/*,"12345678"*/);
        }
        else{

            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("maria_leopoldina@email.pt", "12345678");

            //todo - sincronização threads
            while(!loginTask.isComplete())
                Thread.sleep(1);

            if(loginTask.isSuccessful()){
                DatabaseReference users = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                if(users==null){
                    UsersManager.INSTANCE.addUserToDatabase("Maria Leopoldina","maria_leopoldina@email.pt"/*,"12345678"*/);
                }
            }
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
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("maria_juventina@email.com", "12345678");

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
