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

    UsersManager() {

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //mDatabase.keepSynced(true);

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

    public Map<String,Integer> validateUserCredentialsAndName(String email, String password, String name,String confirmationPass) {

        Map<String,Integer> errorMap=this.validadeUserCredentials(email,password);

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
            return errorMap;
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

    public FindPreference getFindPreferenceByPreferenceString(String preference){
        if(preference.equalsIgnoreCase("Closer To My Location"))
            return FindPreference.CLOSER_LOCATION;

        if(preference.equalsIgnoreCase("Best Rated Place"))
            return FindPreference.BEST_RATED;

        if(preference.equalsIgnoreCase("My Favourite Spots"))
            return FindPreference.FAVOURITE_SPOTS;

        return null;
    }

    public void addFinPreferenceToAUser(String id, FindPreference findPreference) {
        mDatabase.child(id).child("findPreference").setValue(findPreference);
    }

    public Map<String, Integer> validateNameAndEmail(String name, String email) {

        Map<String,Integer> errorMap=new HashMap<>();

        if(name.isEmpty() && email.isEmpty()){
            errorMap.put("email", R.string.emptyEmail);
            errorMap.put("name",R.string.emptyName);
            return errorMap;
        }

        if(name.isEmpty()){
            errorMap.put("name",R.string.emptyName);
            return errorMap;
        }

        if(email.isEmpty()){
            errorMap.put("email", R.string.emptyEmail);
            return errorMap;
        }

        if(android.text.TextUtils.isDigitsOnly(name)) {
            errorMap.put("name",R.string.invalidName);
            return errorMap;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            errorMap.put("email",R.string.invalidEmail);
        }
        return errorMap;
    }

    public void updateUserNameInDatabase(String name) {
        String id=mAuth.getCurrentUser().getUid();
        mDatabase.child(id).child("name").setValue(name);
    }

    public void updateUserEmailInDatabase(String email) {
        String id=mAuth.getCurrentUser().getUid();
        mDatabase.child(id).child("email").setValue(email);

        //Fazer update no firebase
    }

    public void updateUserFindPreferenceInDatabase(String selectPreference) {
        String id=mAuth.getCurrentUser().getUid();
        FindPreference findPreference = this.getFindPreferenceByPreferenceString(selectPreference);

        if(findPreference==null){
            mDatabase.child(id).child("findPreference").removeValue();
        }
        else{
            mDatabase.child(id).child("findPreference").setValue(findPreference);
        }

    }
}
