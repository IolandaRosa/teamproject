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

import groupf.taes.ipleiria.spots.DashboardActivity;
import modelo.FindPreference;
import modelo.UsersManager;
import steps.US5FeatureWithPreferencesSteps;

import static android.os.SystemClock.sleep;

@RunWith(Parameterized.class)
public class US5FeatureWithPreferencesTest extends GreenCoffeeTest {

    @Rule
    public ActivityTestRule activityTestRule=new ActivityTestRule(DashboardActivity.class);

    public US5FeatureWithPreferencesTest(ScenarioConfig scenario) {
        super(scenario);
    }

    @Parameterized.Parameters (name = "{0}")
    public static Collection<ScenarioConfig> data() throws IOException {
        return new GreenCoffeeConfig().withFeatureFromAssets("assets/features/featureUS5WithPreferences.feature").scenarios();
    }

    @Test
    public void test() {
        start(new US5FeatureWithPreferencesSteps());
    }

    //Assegurar que existe o utilizador de teste na BD Auth e na BD de Users
    @BeforeClass
    public static void setUpOnlyOnce() throws Exception {

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            FirebaseAuth.getInstance().signOut();

        //Criar um utilizador manel@email.pt com password "12345678"
        Task<AuthResult> registerTask = UsersManager.INSTANCE.registerUser("manel@email.pt", "12345678");

        //todo - não é a melhor solução mas em termos de performance é melhor que sleep
        while(!registerTask.isComplete())
            Thread.sleep(1);

        //apos obter a resposta se correu como esperado adicionar user à database (era porque utilizador não existia)
        if(registerTask.isSuccessful()){
            UsersManager.INSTANCE.addUserToDatabase("Manel","manel@email.pt"/*,"12345678"*/);
            //Colocar as preferencias no Manel
            UsersManager.INSTANCE.addFindPreferenceToAUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),FindPreference.BEST_RATED);
            //todo tratar caso do sleep para sincronização de threads
            sleep(1000);
        }
        else{
            //Se não for successfull significa que email ja existia e podemos fazer login
            //todo Temos de tratar da excepção caso a password não seja  a mesma ou podemos supor que este é um utilizador de teste apenas e que é assim??

            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("manel@email.pt", "12345678");

            //todo - não é a melhor solução mas em termos de performance é melhor que sleep
            while(!loginTask.isComplete())
                Thread.sleep(1);

            if(loginTask.isSuccessful()){
                //Temos de ver se utilizador já existe na Bd e se não existir acrescentar um novo
                DatabaseReference users = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                if(users==null){
                    UsersManager.INSTANCE.addUserToDatabase("Manel","manel@email.pt"/*,"12345678"*/);
                    //Colocar as preferencias no Manel
                    UsersManager.INSTANCE.addFindPreferenceToAUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),FindPreference.BEST_RATED);
                    //todo tratar caso do sleep para sincronização de threads
                    sleep(1000);
                }
                //se não é porque já existe e não temos de fazer nada

            }
        }
    }

    //Apagar esse user de teste da BD auth e da BD de Users
    @AfterClass
    public static void tearDownOnlyOnce() throws Throwable {
        //Se utilizador estiver logado então simplesmente eliminar
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();
            currentUser.delete();

            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();
        }else{
            //Se não fazer login - não deve acontecer em principio ele esta logado sempre - e eliminar
            Task<AuthResult> loginTask = UsersManager.INSTANCE.makeLogin("manel@email.pt", "12345678");

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
