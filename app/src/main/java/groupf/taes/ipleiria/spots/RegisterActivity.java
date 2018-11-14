package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Map;

import modelo.InternetConnectionManager;
import modelo.UsersManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private EditText editName;
    private EditText editConfirmationPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(DashboardAuthActivity.getIntent(this));
        }

        this.editEmail=findViewById(R.id.editTextEmail);
        this.editPassword=findViewById(R.id.editTextPassword);
        this.editName=findViewById(R.id.editTextName);
        this.editConfirmationPassword=findViewById(R.id.editTextConfirmationPassword);
    }

    public void onClick_btnRegister(View view) {

        if(! InternetConnectionManager.INSTANCE.checkInternetConnection(this)){
            InternetConnectionManager.INSTANCE.showErrorMessage(this,R.string.noInternetConnection);
            return;
        }

        final String email=editEmail.getText().toString().trim();
        final String password=editPassword.getText().toString().trim();
        final String name=editName.getText().toString().trim();
        final String confirmationPass=editConfirmationPassword.getText().toString().trim();

        Map<String, Integer> errorMap = UsersManager.INSTANCE.validateUserCredentialsAndName(email, password, name,confirmationPass);

        if(errorMap.containsKey("email") && errorMap.containsKey("password") && errorMap.containsKey("name") && errorMap.containsKey("confirmationPass")){
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

        if(errorMap.containsKey("name")){
            editName.setError(getResources().getString(errorMap.getOrDefault("name",-1)));
            editName.requestFocus();
        }

        if(errorMap.containsKey("confirmationPass")){
            editConfirmationPassword.setError(getResources().getString(errorMap.getOrDefault("confirmationPass",-1)));
            editConfirmationPassword.requestFocus();
        }

        if(errorMap.isEmpty()) {

            //Regista o utilizador e guarda o utilizador como uma instancia na BD
            Task<AuthResult> authResultTask = UsersManager.INSTANCE.registerUser(email, password);
            authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        //#############################
                        UsersManager.INSTANCE.addUserToDatabase(name,email);
                        //#############################

                        InternetConnectionManager.INSTANCE.showErrorMessage(RegisterActivity.this,R.string.registerSuccess);

                        /*if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            Toast.makeText(RegisterActivity.this,"User not null",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"User null",Toast.LENGTH_LONG).show();
                        }*/


                        //todo ir para starNewActivityLoged e mostrar welcome message
                    }else{

                        if(task.getException() instanceof FirebaseAuthUserCollisionException )
                        {
                            InternetConnectionManager.INSTANCE.showErrorMessage(RegisterActivity.this,R.string.userColision);
                        }
                    }
                }
            });
        }

    }

    public void onClick_btnAlreadyAuth(View view) {

        startActivity(LoginActivity.getIntent(this));
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

}
