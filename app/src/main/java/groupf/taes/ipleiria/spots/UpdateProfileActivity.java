package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import modelo.User;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private Spinner spinnerPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(DashboardActivity.getIntent(this));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        this.editTextName=findViewById(R.id.editTextName);
        this.editTextEmail=findViewById(R.id.editTextEmail);
        this.spinnerPreferences=findViewById(R.id.spinnerFindPreference);

        this.initializeFields();

    }

    public static Intent getIntent(Context context, User user) {
        Intent intent= new Intent(context, UpdateProfileActivity.class);
        intent.putExtra("name",user.getName());
        intent.putExtra("email",user.getEmail());
        intent.putExtra("preference", user.getFindPreference());

        return intent;
    }

    private void initializeFields(){

        this.editTextName.setText(getIntent().getStringExtra("name"));
    }
}
