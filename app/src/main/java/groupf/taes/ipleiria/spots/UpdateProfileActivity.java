package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import modelo.InternetConnectionManager;
import modelo.User;
import modelo.UsersManager;

import static android.os.SystemClock.sleep;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private Spinner spinnerPreferences;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(DashboardActivity.getIntent(this));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        this.editTextName = findViewById(R.id.editTextName);
        this.editTextEmail = findViewById(R.id.editTextEmail);
        this.spinnerPreferences = findViewById(R.id.spinnerFindPreference);
        this.editTextPassword=findViewById(R.id.editTextPassword);

        this.initializeFields();

    }

    public static Intent getIntent(Context context, User user) {
        Intent intent = new Intent(context, UpdateProfileActivity.class);
        intent.putExtra("name", user.getName());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("preference", UsersManager.INSTANCE.toStringPreference(user.getFindPreference()));

        return intent;
    }

    private void initializeFields() {

        this.editTextName.setText(getIntent().getStringExtra("name"));
        this.editTextEmail.setText(getIntent().getStringExtra("email"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.findPreferencesOptions, android.R.layout.simple_spinner_item);

        spinnerPreferences.setAdapter(adapter);

        int position = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().compareToIgnoreCase(getIntent().getStringExtra("preference")) == 0) {
                position = i;
                break;
            }
        }

        spinnerPreferences.setSelection(position, true);
    }

    public void onClick_btnCancel(View view) {
        startActivity(ProfileActivity.getIntent(this));
    }


    public void onClick_btnSave(View view) {
        String name = this.editTextName.getText().toString();
        String email = this.editTextEmail.getText().toString();

        Map<String, Integer> errorMap = UsersManager.INSTANCE.validateNameAndEmail(name, email);

        if (errorMap.containsKey("email") && errorMap.containsKey("name")) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.emptyFields);
            return;
        }

        if (errorMap.containsKey("email")) {
            editTextEmail.setError(getResources().getString(errorMap.getOrDefault("email", -1)));
            editTextEmail.requestFocus();
        }

        if (errorMap.containsKey("name")) {
            editTextName.setError(getResources().getString(errorMap.getOrDefault("name", -1)));
            editTextName.requestFocus();
        }

        String selectPreference = spinnerPreferences.getSelectedItem().toString();

        boolean nameEquals=this.getIntent().getStringExtra("name").equalsIgnoreCase(name);

        boolean emailEquals=this.getIntent().getStringExtra("email").equalsIgnoreCase(email);

        boolean preferenceEquals=this.getIntent().getStringExtra("preference").equalsIgnoreCase(selectPreference);

        if(emailEquals && preferenceEquals && nameEquals){
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.noChanges);
            return;
        }

        if(!nameEquals && (!emailEquals || !preferenceEquals)){
            UsersManager.INSTANCE.updateUserNameInDatabase(name);
            findViewById(R.id.confirmationLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.btnSave).setVisibility(View.GONE);
            return;
        }

        if(!emailEquals || !preferenceEquals){
            findViewById(R.id.confirmationLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.btnSave).setVisibility(View.GONE);
            return;
        }

        if(!nameEquals){
            UsersManager.INSTANCE.updateUserNameInDatabase(name);
            startActivity(ProfileActivity.getIntent(this));
        }




    }


    public void onClick_btnOk(View view) {
        final String password = editTextPassword.getText().toString();

        if(password.isEmpty()){
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.emptyPassword);
        }
        else{

            AuthCredential credentials = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(),password);

            FirebaseAuth.getInstance().getCurrentUser().reauthenticateAndRetrieveData(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String email = editTextEmail.getText().toString();
                        String selectPreference = spinnerPreferences.getSelectedItem().toString();

                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email);
                        UsersManager.INSTANCE.updateUserEmailInDatabase(email);
                        UsersManager.INSTANCE.updateUserFindPreferenceInDatabase(selectPreference);
                        startActivity(ProfileActivity.getIntent(UpdateProfileActivity.this));
                    }
                    else{
                        InternetConnectionManager.INSTANCE.showErrorMessage(UpdateProfileActivity.this,R.string.invalidPassword);
                    }
                }
            });





            /*DatabaseReference passwordRef=UsersManager.INSTANCE.getCurrentUserPasswordReference();



            passwordRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String userPass=dataSnapshot.getValue(String.class);

                    if(!userPass.equals(UsersManager.INSTANCE.md5_Hash(password))){
                        InternetConnectionManager.INSTANCE.showErrorMessage(UpdateProfileActivity.this,R.string.invalidPassword);
                    }
                    else{
                        final String email = editTextEmail.getText().toString();
                        final String selectPreference = spinnerPreferences.getSelectedItem().toString();

                        Toast.makeText(UpdateProfileActivity.this,userPass,Toast.LENGTH_LONG).show();*/

                        /*AuthCredential credentials = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(),password);
                        Task<Void> task = FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credentials);

                        try{
                            Tasks.await(task);

                            Tasks.await(FirebaseAuth.getInstance().getCurrentUser().updateEmail(email));

                            UsersManager.INSTANCE.updateUserEmailInDatabase(email);
                            UsersManager.INSTANCE.updateUserFindPreferenceInDatabase(selectPreference);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }*/

                        /*
                        Task<Void> voidTask = FirebaseAuth.getInstance().getCurrentUser().updateEmail(email);

                        //todo - change
                        sleep(5000);

                        voidTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //Toast.makeText(UpdateProfileActivity.this,"1 - Email Updated",Toast.LENGTH_LONG).show();
                                    UsersManager.INSTANCE.updateUserEmailInDatabase(email);
                                    UsersManager.INSTANCE.updateUserFindPreferenceInDatabase(selectPreference);
                                    startActivity(ProfileActivity.getIntent(UpdateProfileActivity.this));
                                }else{

                                    AuthCredential credentials = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(),password);
                                    Task<Void> reauthenticate = FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credentials);

                                    //todo - change
                                    sleep(5000);

                                    reauthenticate.addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Task<Void> updateTask = FirebaseAuth.getInstance().getCurrentUser().updateEmail(email);

                                                //todo - change
                                                sleep(5000);

                                                updateTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            //Toast.makeText(UpdateProfileActivity.this,"2 - Email Updated",Toast.LENGTH_LONG).show();
                                                            UsersManager.INSTANCE.updateUserEmailInDatabase(email);
                                                            UsersManager.INSTANCE.updateUserFindPreferenceInDatabase(selectPreference);
                                                            startActivity(ProfileActivity.getIntent(UpdateProfileActivity.this));
                                                        }
                                                        else{
                                                            Toast.makeText(UpdateProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });


                                            }
                                            else{
                                                Toast.makeText(UpdateProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });*/

                   /* }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
        }
    }
}
