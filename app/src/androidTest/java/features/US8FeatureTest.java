package features;

import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
import modelo.UsersManager;
import steps.US8FeatureSteps;

@RunWith(Parameterized.class)
public class US8FeatureTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(DashboardAuthActivity.class);

    public US8FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS8.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US8FeatureSteps());
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("changePass@email.pt", "12345678");

        //todo - não é a melhor solução mas em termos de performance é melhor que sleep
        while(!registerTask.isComplete())
            Thread.sleep(1);

        if(!registerTask.isSuccessful()){
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("changePass@email.pt", "12345678");

            //todo - não é a melhor solução mas em termos de performance é melhor que sleep
            while(!loginTask.isComplete())
                Thread.sleep(1);
        }
    }


    @AfterClass
    public static void tearDownOnlyOnce() throws Throwable {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseAuth.getInstance().getCurrentUser().delete();
        }else{
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("changePass@email.pt", "12345677");

            //todo - não é a melhor solução mas em termos de performance é melhor que sleep
            while(!loginTask.isComplete())
                Thread.sleep(1);

            if(loginTask.isSuccessful()){
                FirebaseAuth.getInstance().getCurrentUser().delete();
            }
        }
    }

}
