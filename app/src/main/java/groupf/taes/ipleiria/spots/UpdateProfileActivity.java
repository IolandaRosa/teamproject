package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import modelo.InternetConnectionManager;
import modelo.User;
import modelo.UsersManager;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private Spinner spinnerPreferences;
    private User user;

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

        if(!nameEquals){
            UsersManager.INSTANCE.updateUserNameInDatabase(name);
        }

        if(!emailEquals){
            //pede autenticação com password
            //se password corresponder à do utilizador
                //UsersManager.INSTANCE.updateUserEmailInDatabase(email);
                //atualiza email na autenticação do firebase
            //senão
                //dá erro
        }

        if(!preferenceEquals){
            //pede autenticação com password
            //se password corresponder à do utilizador
                //UsersManager.INSTANCE.updateUserFindPreferenceInDatabase(selectPreference);
            //senão
                //dá erro
        }

        Toast.makeText(this,String.valueOf(preferenceEquals),Toast.LENGTH_LONG).show();
    }
}
