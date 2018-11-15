package features;

import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.Scenario;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import groupf.taes.ipleiria.spots.LoginActivity;
import steps.US3FeatureSteps;

import static android.os.SystemClock.sleep;

@RunWith(Parameterized.class)
public class US3FeatureTest extends GreenCoffeeTest {


    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(LoginActivity.class);

    public US3FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS3.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US3FeatureSteps());
    }

    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
        super.beforeScenarioStarts(scenario, locale);

        //Cada cenário inicia sempre com o utilizador anónimo
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();
    }

    //No final de cada teste como estamos sempre a resgistar o mesmo utilizador e ele fica logo logado então elimina-se esse utilizador
    //da Bd de autenticação e da BD mesmo
    @After
    public void testStart() throws Throwable {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();
            currentUser.delete();

            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
        }
    }

    //Antes e iniciar a classe vamos criar um utilizador que não existe na BD para fazer o teste do registo de um utilizador que já esta registado
    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {
        //Criar um utilizador test@test.test com password "12345678"
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        //2º - Ver se o utilizador já existe (em principio não deve existir)
        Task<AuthResult> registerTask = FirebaseAuth.getInstance().createUserWithEmailAndPassword("test@test.test", "12345678");
        //todo tratar caso do sleep para sincronização de threads
        sleep(5000);
        //apos obter a resposta se for sucessful correu como esperado e é so fazer signout
        if(registerTask.isSuccessful()){
            if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                FirebaseAuth.getInstance().signOut();
        }
        else{
            //Se não for successfull significa que email ja existia e podemos fazer login
            //todo Temos de tratar da excepção caso a password não seja  a mesma ou podemos supor que este é um utilizador de teste apenas e que é assim??
        }
    }

    //No final apagar esse utilizador criado no inicio
    @AfterClass
    public static void tearDownOnlyOnce() throws Exception {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseAuth.getInstance().signOut();
        }
        //Destruir o utilizador test@test.test com password "12345678"
        FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.test","12345678");
        sleep(5000);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseAuth.getInstance().getCurrentUser().delete();
        }

    }
}
