package modelo;

import android.util.Patterns;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import groupf.taes.ipleiria.spots.R;

public enum UsersManager {
    INSTANCE;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Map<String,Integer> errorMap;

    UsersManager() {

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //mDatabase.keepSynced(true);

        this.mAuth=FirebaseAuth.getInstance();
        this.errorMap=new HashMap<>();
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

    public Map<String,Integer> validateUserCredentialsAndName(String email, String password, String name,String confirmationPass) {

        Map<String,Integer> errorMap=this.validadeUserCredentials(email,password);

        //boolean flag = false;

        if(confirmationPass.isEmpty())
        {
            errorMap.put("confirmationPass",R.string.emptyConfirmationPass);

        }
        if(name.isEmpty()){
            errorMap.put("name",R.string.emptyName);
            return errorMap;
        }

        if(!confirmationPass.isEmpty() && !password.equals(confirmationPass))
        {
            errorMap.put("confirmationPass",R.string.errorConfirmationPass);
        }

        if(android.text.TextUtils.isDigitsOnly(name)){
            errorMap.put("name",R.string.invalidName);
        }

        return errorMap;
    }

    public Task<AuthResult> registerUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public void addUserToDatabase(String name, String email) {
        String id=mAuth.getCurrentUser().getUid();
        User user=new User(id,name,email,null);
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

    public void addFinPreferenceToAUser(String id, FindPreference findPreference) {
        mDatabase.child(id).child("findPreference").setValue(findPreference);
    }

    public void validateName(String name) {
        if(name.isEmpty()){
            errorMap.put("name",R.string.emptyName);
        }
        else if(android.text.TextUtils.isDigitsOnly(name)) {
            errorMap.put("name",R.string.invalidName);
        }
    }

    public void validateEmail(String email) {
        if(email.isEmpty()){
            errorMap.put("email",R.string.emptyEmail);
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            errorMap.put("email",R.string.invalidEmail);
        }
    }

    public Map<String, Integer> getErrorMap() {
        return errorMap;
    }
}
