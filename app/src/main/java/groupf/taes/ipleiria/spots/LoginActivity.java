package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.io.Console;
import java.util.Map;

import modelo.UsersManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.editEmail=findViewById(R.id.editTextEmail);
        this.editPassword=findViewById(R.id.editTextPassword);
    }

    public void onClick_buttonLogin(View view) {
        String email=editEmail.getText().toString().trim();
        String password=editPassword.getText().toString().trim();

        Map<String, Integer> errorMap = UsersManager.INSTANCE.validadeUserCredentials(email, password);

        if(errorMap.containsKey("email") && errorMap.containsKey("password")){
            //UsersManager.INSTANCE.showErrorMessage(this,R.string.emptyFields);
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

            resultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //starNewActivityLoged
                    }else{
                        //Acho que os ifs são obsoletos chegando neste ponto quase de certeza que são credencias erradas e se não for tambem não sei ate que ponto fara sentido o utilizador receber a excepção
                        //so faz sentido fazer isto se quisermos guardar informação dos erros em log...
                        //UsersManager.INSTANCE.showErrorMessage(LoginActivity.this,R.string.invalidCredentials);
                        /*if(task.getException() instanceof FirebaseAuthInvalidCredentialsException ||task.getException() instanceof FirebaseAuthInvalidUserException){
                                UsersManager.INSTANCE.showErrorMessage(LoginActivity.this,R.string.invalidCredentials);
                        }
                        else{
                            UsersManager.INSTANCE.showErrorMessage(LoginActivity.this,R.string.invalidCredentials);
                            //Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }
            });
        }
    }

    public void onClick_buttonRegister(View view) {
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }


}
