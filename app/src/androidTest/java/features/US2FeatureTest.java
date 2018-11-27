package features;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

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
import steps.US2FeatureSteps;

@RunWith(Parameterized.class)
public class US2FeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(DashboardActivity.class);

    public US2FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS2.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US2FeatureSteps());
    }

    //Antes de cada teste do login faz sign out
    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

    }

    //Antes da classe iniciar cria um utilizador para testar
    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {
        //Criar um utilizador test@test.test com password "12345678"
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        Task<AuthResult> registerTask = FirebaseAuth.getInstance().createUserWithEmailAndPassword("test@test.test", "12345678");

        //todo - aplicar sincronização
        while (!registerTask.isComplete())
            Thread.sleep(1);

        if(registerTask.isSuccessful()){
            if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                FirebaseAuth.getInstance().signOut();
        }
        else{
            Exception exception = registerTask.getException();
            //Se não for successfull significa que email ja existia e podemos fazer login
            Log.println(1,"Exception set Up",exception.getMessage());
        }
    }

    @AfterClass
    public static void tearDownOnlyOnce() throws Exception {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseAuth.getInstance().signOut();
        }
        //Destruir o utilizador test@test.test com password "12345678"
        Task<AuthResult> authResultTask = FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.test", "12345678");

        //todo - aplicar sincronização
        while(!authResultTask.isComplete())
            Thread.sleep(1);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseAuth.getInstance().getCurrentUser().delete();
        }
    }
}
