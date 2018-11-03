package modelo;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;

import com.google.android.gms.common.internal.ResourceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import groupf.taes.ipleiria.spots.R;

public enum UsersManager {
    INSTANCE;

    private FirebaseAuth mAuth;

    UsersManager() {
        this.mAuth=FirebaseAuth.getInstance();
    }


    public Task<AuthResult> makeLogin(String email, String password) {

        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Map<String, Integer> validadeUserCredentials(String email, String password) {

        Map<String,Integer> errorMap=new HashMap<>();

        if(email.isEmpty() && password.isEmpty()){
            errorMap.put("email", R.string.emptyEmail);
            errorMap.put("password",R.string.emptyPassword);

            return errorMap;
        }

        if(email.isEmpty()){
            errorMap.put("email",R.string.emptyEmail);
            return errorMap;
        }

        if(password.isEmpty()){
            errorMap.put("password",R.string.emptyPassword);
            return errorMap;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            errorMap.put("email",R.string.invalidEmail);
            return errorMap;
        }

        if(password.length()<8){
            errorMap.put("password",R.string.invalidPasswordLength);

        }

        return errorMap;
    }

    public void showErrorMessage(Context context, int message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        builder.setTitle(message);

        builder.show();
    }
}
