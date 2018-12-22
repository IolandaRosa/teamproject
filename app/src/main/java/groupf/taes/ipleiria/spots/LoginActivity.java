package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import modelo.InternetConnectionManager;
import modelo.UsersManager;

public class LoginActivity extends AppCompatActivity {
    private static final CountingIdlingResource idlingResource = new CountingIdlingResource("login");
    private EditText editEmail;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(DashboardAuthActivity.getIntent(this));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        this.editEmail=findViewById(R.id.editTextEmail);
        this.editPassword=findViewById(R.id.editTextPassword);
    }

    public void onClick_buttonLogin(View view) {

        if(! InternetConnectionManager.INSTANCE.checkInternetConnection(this)){
            InternetConnectionManager.INSTANCE.showErrorMessage(this,R.string.noInternetConnection);
            return;
        }

        String email=editEmail.getText().toString().trim();
        String password=editPassword.getText().toString().trim();

        Map<String, Integer> errorMap = UsersManager.INSTANCE.validadeUserCredentials(email, password);

        if(errorMap.containsKey("email") && errorMap.containsKey("password")){
            InternetConnectionManager.INSTANCE.showErrorMessage(this,R.string.emptyFields);
            return;
        }

        if(errorMap.containsKey("email")){
            editEmail.setError(getResources().getString(errorMap.getOrDefault("email",-1)));
            editEmail.requestFocus();
        }

        if(errorMap.containsKey("password")){
            editPassword.setError(getResources().getString(errorMap.getOrDefault("password",-1)));
            editPassword.requestFocus();
        }

        if(errorMap.isEmpty()){
            Task<AuthResult> resultTask = UsersManager.INSTANCE.makeLogin(email, password);
            idlingResource.increment();
            resultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    idlingResource.decrement();
                    if(task.isSuccessful()){
                        //DashboardActivity.dashActivity.finish();
                        Intent i  = DashboardAuthActivity.getIntent(LoginActivity.this);
                        i.putExtra("EXECUTE_READ_SPOTS",false);
                        startActivity(i);

                    }else{
                        InternetConnectionManager.INSTANCE.showErrorMessage(LoginActivity.this,R.string.invalidCredentials);
                    }
                }
            });
        }
    }

    public void onClick_buttonRegister(View view) {
        startActivity(RegisterActivity.getIntent(this));
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

}
