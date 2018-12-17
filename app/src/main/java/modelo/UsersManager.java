package modelo;

import android.util.Patterns;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import groupf.taes.ipleiria.spots.R;

public enum UsersManager {
    INSTANCE;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private User currentUser;

    UsersManager() {

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

    public void addUserToDatabase(String name, String email/*, String password*/) {
        String id=mAuth.getCurrentUser().getUid();
        User user=new User(id,name,email,null,null/*, password*/);
        /*List<Spot> spots=new ArrayList<>();
        spots.add(new Spot("A-1","D","1,2",0,4));
        spots.add(new Spot("A-2","D","1,2",0,0));
        spots.add(new Spot("A-3","D","1,2",0,4));
        spots.add(new Spot("A-4","D","1,2",0,0));
        spots.add(new Spot("A-5","D","1,2",0,4));
        spots.add(new Spot("A-6","D","1,2",0,0));
        user.setFavouriteSpots(spots);*/
        mDatabase.child(id).setValue(user);
    }

    // para testes
    public void addUserThatIsParked(String name, String email, String spotId/*, String password*/) {
        String id=mAuth.getCurrentUser().getUid();
        User user=new User(id,name,email,null, spotId/*, password*/);
        mDatabase.child(id).setValue(user);
    }

    //Usado apenas para testes
    public void addUserWithSpotsToDatabase(String name,String email,List<Spot> spots){
        String id=mAuth.getCurrentUser().getUid();
        User user=new User(id,name,email,null,null);
        user.setFavouriteSpots(spots);
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

        if(preference==FindPreference.FAVOURITE_SPOTS)
            return "My Favourite Spots";

        return "None";
    }

    public FindPreference getFindPreference(String preference){
        if(preference.equalsIgnoreCase("CLOSER_LOCATION"))
            return FindPreference.CLOSER_LOCATION;

        if(preference.equalsIgnoreCase("BEST_RATED"))
            return FindPreference.BEST_RATED;

        if(preference.equalsIgnoreCase("FAVOURITE_SPOTS"))
            return FindPreference.FAVOURITE_SPOTS;

        return null;
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

    public void addFindPreferenceToAUser(String id, FindPreference findPreference) {
        mDatabase.child(id).child("findPreference").setValue(findPreference);
    }

    public Map<String, Integer> validateNameAndEmail(String name, String email) {

        Map<String,Integer> errorMap=new HashMap<>();

        if(name.trim().isEmpty() && email.trim().isEmpty()){
            errorMap.put("email", R.string.emptyEmail);
            errorMap.put("name",R.string.emptyName);
            return errorMap;
        }

        if(name.trim().isEmpty()){
            errorMap.put("name",R.string.emptyName);
            return errorMap;
        }

        if(email.trim().isEmpty()){
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

    public void logoutUser() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            FirebaseAuth.getInstance().signOut();
        }
    }

    public Map<String, Integer> validateChangePassword(String oldPassword, String newPassword, String newPasswordConfirmation) {

        Map<String,Integer> errorMap=new HashMap<>();

        if(oldPassword.trim().isEmpty() && newPassword.trim().isEmpty() && newPasswordConfirmation.trim().isEmpty()){
            errorMap.put("emptyFields",R.string.emptyFields);
            return errorMap;
        }

        if(oldPassword.trim().isEmpty()){
            errorMap.put("currentPassword",R.string.emptyPassword);
            return errorMap;
        }

        if(oldPassword.length() < 8){
            errorMap.put("currentPassword",R.string.invalidPasswordLength);
            return errorMap;
        }

        if(newPassword.trim().isEmpty()){
            errorMap.put("newPassword",R.string.emptyPassword);
            return errorMap;
        }

        if(newPassword.length() < 8){
            errorMap.put("newPassword",R.string.invalidPasswordLength);
            return errorMap;
        }

        if(newPasswordConfirmation.trim().isEmpty()){
            errorMap.put("newPasswordConfirmation",R.string.emptyConfirmationPass);
            return errorMap;
        }


        if(!newPassword.equals(newPasswordConfirmation)){
            errorMap.put("passwordMismatch",R.string.errorConfirmationPass);
            return errorMap;
        }

        if(oldPassword.equals(newPassword)){
            errorMap.put("newInvalidPassword",R.string.errorNewPasswordEquals);
            return errorMap;
        }

        return errorMap;
    }

    //Não utilizados
    public DatabaseReference getCurrentUserPasswordReference() {
        String id=mAuth.getCurrentUser().getUid();

        return mDatabase.child(id).child("password");
    }

    public String md5_Hash(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    public void loadCurrentUser (DatabaseReference ref) {
        //DatabaseReference ref = this.getUserProfileInfo();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot ds) {
               // currentUser = dataSnapshot.getValue(User.class);
                FindPreference preference=null;
                String id=null;
                String name=null;
                String email=null;
                String spotParked = null;

                Object idObject = ds.child("id").getValue();
                if (idObject != null) {
                    id = idObject.toString();
                }

                Object nameObject = ds.child("name").getValue();
                if (nameObject != null) {
                    name = nameObject.toString();
                }

                Object emailObject = ds.child("email").getValue();
                if (emailObject != null) {
                    email = emailObject.toString();
                }

                Object preferenceObject = ds.child("findPreference").getValue();
                if (preferenceObject != null) {
                   // String str = preferenceObject.toString();
                    preference = UsersManager.INSTANCE.getFindPreference(preferenceObject.toString());
                }

                Object spotParkedObject = ds.child("spotParked").getValue();
                if (spotParkedObject != null) {
                    spotParked = spotParkedObject.toString();
                }

                currentUser = new User(id, name, email, preference, spotParked);
                ArrayList<Spot> favouriteSpots = new ArrayList<Spot>();

                for (DataSnapshot d : ds.child("favouriteSpots").getChildren()) {
                    Spot s = d.getValue(Spot.class);

                    favouriteSpots.add(s);
                }
                currentUser.setFavouriteSpots(favouriteSpots);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public DatabaseReference getFavouriteSpotsList() {

        String id=mAuth.getCurrentUser().getUid();

        return mDatabase.child(id).child("favouriteSpots");
    }

    public void addFavouriteSpotsList(User user, Spot spot) {
        user.addFavouriteSpot(spot);
        List<Spot> userSpots = user.getFavouriteSpots();
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("favouriteSpots").setValue(userSpots);
    }

    public void setSpotUserIsParked(String spotId) {
      //  mDatabase.child(mAuth.getCurrentUser().getUid()).child("spotParked").setValue(spotId);
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("spotParked").setValue(spotId);
    }

}
