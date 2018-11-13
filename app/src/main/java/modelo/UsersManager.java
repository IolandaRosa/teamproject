package modelo;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import groupf.taes.ipleiria.spots.R;

public enum UsersManager {
    INSTANCE;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    UsersManager() {
        this.mAuth=FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.mDatabase = FirebaseDatabase.getInstance().getReference("users");

        //mDatabase.keepSynced(true);
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

    public Task<AuthResult> registerUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public void addUserToDatabase(String name, String email) {
        String id=mAuth.getCurrentUser().getUid();
        User user=new User(id,name,email,FindPreference.CLOSER_LOCATION);
        mDatabase.child(id).setValue(user);
    }

    public DatabaseReference getUserProfileInfo() {

        return mDatabase.child(mAuth.getCurrentUser().getUid());
    }

    public String toStringPreference(FindPreference preference) {
        if(preference==FindPreference.CLOSER_LOCATION)
            return "Closer To My Location";

        if(preference==FindPreference.BEST_RATED)
            return "Best Rated Place";

        return "My Favourite Spots";
    }

}
