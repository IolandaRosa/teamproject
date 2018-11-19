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

import groupf.taes.ipleiria.spots.DashboardAuthActivity;
import modelo.UsersManager;
import steps.US4FeatureSteps;
import steps.US6FeatureSteps;

import static android.os.SystemClock.sleep;

@RunWith(Parameterized.class)
public class US6FeatureTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(DashboardAuthActivity.class);

    public US6FeatureTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS6.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US6FeatureSteps());
    }

    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {
        //Criar um utilizador maria@email.pt com password "12345678"
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        //2º - Ver se o utilizador já existe (em principio não deve existir)
        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("mariaTest@email.pt", "12345678");
        //todo tratar caso do sleep para sincronização de threads
        sleep(5000);
        //apos obter a resposta se for sucessful correu como esperado e é so fazer signout
        if(registerTask.isSuccessful()){
            //Quer dizer que utilizador não existia então acrescenta utilizador na BD
            UsersManager.INSTANCE.addUserToDatabase("Maria Pt","mariaTest@email.pt");
            //Utilizador já fica logado e aplicação pode iniciar no authenticated dashboard
        }
        else{
            //Se não for successfull significa que email ja existia e podemos fazer login
            //todo Temos de tratar da excepção caso a password não seja  a mesma ou podemos supor que este é um utilizador de teste apenas e que é assim??

            //Fazemos login
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("mariaTest@email.pt", "12345678");

            //todo tratar caso do sleep para sincronização de threads
            sleep(5000);
            if(loginTask.isSuccessful()){
                //Temos de ver se utilizador já existe na Bd e se não existir acrescentar
                DatabaseReference users = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                if(users==null){
                    UsersManager.INSTANCE.addUserToDatabase("Maria Pt","mariaTest@email.pt");
                }
                //se não é porque já existe e não temos de fazer nada

            }
        }
    }

    //Apagar esse user de teste da BD auth e da BD de Users
    @AfterClass
    public static void tearDownOnlyOnce() throws Throwable {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();
            currentUser.delete();

            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
        }else{
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("maria@email.pt", "12345678");

            //todo tratar caso do sleep para sincronização de threads
            sleep(5000);
            if(loginTask.isSuccessful()){
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                currentUser.delete();

                FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
            }
        }
    }

}
